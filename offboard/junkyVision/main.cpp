//Dependencies

//OpenCV 2.4.10
//Boost ASIO
//pthread

#include "opencv2/opencv.hpp"
#include "server.hpp"
#include "pid.hpp"

#include <vector>
#include <thread>
#include <chrono>
#include <iostream>
#include <cstdio>

using namespace cv;

void initUI();
void displayMat(Mat matToDisplay, std::string windowName);
void detectAndDisplay();
void iterateAndDrawContours(std::vector<vector<Point> > contours, Mat target);
void wait(int ms);
double normalize(double number);

// all time units are in ms

bool runHeadless = false;
int loopHz = 25;
int loopDelta = 1000 / loopHz;

int hLow = 70;
int hHigh = 120;
int sLow = 0;
int sHigh = 255;
int vLow = 150;
int vHigh = 255;

double conversionFactor = 1.0f / 150.0f;

int smallestBoxArea = 10000000; // arbitrary large number
int smallestBoxPV = 0; // calculated input for smallest box

float maxAspectRatio = 2.4f;
float minimumArea = 0.2f;
float maximumArea = 1.0f;

VideoCapture capture;
Mat image;

int main(int argc, char** argv) {
	if (!runHeadless) {
		std::cout << "Running with GUI" << std::endl;
		initUI();
	} else {
		std::cout << "Running headless" << std::endl;
	}

	std::thread server(Server::start);
	capture = VideoCapture(0);
	capture.read(image);

	PID pid(0.5, 0, 0, 0, 0, 0, 0);
	smallestBoxPV = ((double) image.cols) / 2.0f;
	pid.SetSetpoint(((double) image.cols) / 2.0f);

	if (!capture.isOpened()) {
		std::cout << "Did not connect to camera.";
	} else {
		while (true) {
			if (!runHeadless) {
				detectAndDisplay();
				waitKey(loopDelta);
			} else {
				std::thread t1(detectAndDisplay);
				std::thread t2(wait, loopDelta);

				t2.join();
				t1.join();
			}

			//TODO: Implement proper PID
			pid.SetInput(smallestBoxPV);
			double newThrottle = -1 * (pid.Update(loopDelta)); // positive value: turn right
			newThrottle = normalize(newThrottle);

			Server::updateThrottle(newThrottle);
		}

	}

	capture.release();

	return 0;
}

void detectAndDisplay() {
	capture.read(image);

	Mat imageCopy = image.clone();
	Mat imageCopyYellow = image.clone();
	Mat imageCopyGrey = image.clone();

	Mat imageHSV;

	cvtColor(image, imageHSV, CV_RGB2HSV);
	GaussianBlur(imageHSV, imageHSV, Size(25, 25), 0.0f, 0.0f);

	Mat maskGrey;
	Mat maskYellow;
	inRange(imageHSV, Scalar(hLow, sLow, vLow), Scalar(hHigh, sHigh, vHigh),
			maskGrey);
	inRange(imageHSV, Scalar(95, 0, 170), Scalar(99, 255, 255), maskYellow);

	dilate(maskGrey, maskGrey, getStructuringElement(MORPH_RECT, Size(5, 5)));
	erode(maskGrey, maskGrey, getStructuringElement(MORPH_RECT, Size(3, 3)));
	dilate(maskYellow, maskYellow,
			getStructuringElement(MORPH_RECT, Size(5, 5)));
	erode(maskYellow, maskYellow,
			getStructuringElement(MORPH_RECT, Size(3, 3)));

	Mat inverter(maskGrey.rows, maskGrey.cols, maskGrey.type(),
			Scalar(255, 255, 255));

	subtract(inverter, maskGrey, maskGrey);
	subtract(inverter, maskYellow, maskYellow);

	imageCopyGrey.setTo(Scalar(0, 0, 0), maskGrey);
	imageCopyYellow.setTo(Scalar(0, 0, 0), maskYellow);

	Canny(imageCopyGrey, imageCopyGrey, 100, 300);
	Canny(imageCopyYellow, imageCopyYellow, 100, 300);

	std::vector<vector<Point> > contoursGrey;
	std::vector<Vec4i> hierarchy;
	std::vector<vector<Point> > contoursYellow;

	findContours(imageCopyGrey, contoursGrey, hierarchy, RETR_LIST,
			CHAIN_APPROX_SIMPLE);
	findContours(imageCopyYellow, contoursYellow, hierarchy, RETR_LIST,
			CHAIN_APPROX_SIMPLE);

	iterateAndDrawContours(contoursGrey, imageCopy);
	iterateAndDrawContours(contoursYellow, imageCopy);
	smallestBoxArea = 10000000; // arbitrary large number

	if (!runHeadless) {
		displayMat(imageCopy, "main");
		//displayMat(imageCopyGrey, "grey");
		//displayMat(imageCopyYellow, "yellow");
	}

}

void iterateAndDrawContours(std::vector<vector<Point> > contours, Mat target) {
	vector<vector<Point> >::iterator contour;

	for (contour = contours.begin(); contour < contours.end(); contour++) {
		Rect bound = boundingRect(Mat(*contour));

		float aspectRatio = (float) bound.width / (float) bound.height;
		int minimumActualArea = (image.rows * image.cols) * minimumArea;
		int maximumActualArea = (image.rows * image.cols) * maximumArea;

		if (aspectRatio < 1) {
			aspectRatio = 1.0f / aspectRatio;
		}

		//std::printf("%f\n", aspectRatio);

		if (bound.area() > minimumActualArea
				&& bound.area() <= maximumActualArea
				&& maxAspectRatio > aspectRatio) {

			rectangle(target, bound.tl(), bound.br(), Scalar(255, 255, 255));

			if (bound.area() < smallestBoxArea) {
				smallestBoxPV = (double) bound.x + ((double) bound.width / 2);
				smallestBoxArea = bound.area();
			}
		}
	}
}

void initUI() {
	namedWindow("main");
	//namedWindow("yellow");
	//namedWindow("grey");
}

void displayMat(Mat matToDisplay, std::string windowName) {
	imshow(windowName, matToDisplay);
}

void wait(int ms) {
	std::this_thread::sleep_for(std::chrono::milliseconds(ms));
}

double normalize(double number) {
	number = number * conversionFactor;
	std::cout << number << std::endl;

	if (number > 1.0f) {
		return 1.0f;
	}

	if (number < -1.0f) {
		return -1.0f;
	}

	return number;
}
