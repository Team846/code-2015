#include "opencv2/opencv.hpp"
#include <vector>
#include <thread>
#include <iostream>
#include <cstdio>

using namespace cv;

void initUI();
void displayMat(Mat matToDisplay, std::string windowName);
void detectAndDisplay();
void iterateAndDrawContours(std::vector<vector<Point> > contours, Mat target);

const int hLow = 70;
const int hHigh = 120;
const int sLow = 0;
const int sHigh = 255;
const int vLow = 150;
const int vHigh = 255;

const float maxAspectRatio = 4.0f;
const int minimumArea = 5000;
const int maximumArea = 100000;

VideoCapture capture;
Mat image;

int main(int argc, char** argv) {
	initUI();

	capture = VideoCapture(0);

	if (!capture.isOpened()) {
		std::cout << "Did not connect to camera.";
	} else {
		while (true) {

			detectAndDisplay();

			waitKey(10);
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

	displayMat(imageCopy, "main");
	displayMat(imageCopyGrey, "grey");
	displayMat(imageCopyYellow, "yellow");
}

void iterateAndDrawContours(std::vector<vector<Point> > contours, Mat target) {
	vector<vector<Point> >::iterator contour;

	for (contour = contours.begin(); contour < contours.end(); contour++) {
		Rect bound = boundingRect(Mat(*contour));

		float aspectRatio = (float) bound.width / (float) bound.height;
		int minimumArea = 100000;
		int maximumArea = 200000;

		//std::printf("%f\n", aspectRatio);

		if (bound.area() > minimumArea && bound.area() <= maximumArea
				&& maxAspectRatio > aspectRatio) {
			rectangle(target, bound.tl(), bound.br(), Scalar(255, 255, 255));
		}
	}
}

void initUI() {
	namedWindow("main");
	namedWindow("yellow");
	namedWindow("grey");
}

void displayMat(Mat matToDisplay, std::string windowName) {
	imshow(windowName, matToDisplay);
}
