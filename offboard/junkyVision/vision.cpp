/*
 * vision.cpp
 *
 *  Created on: Feb 21, 2015
 *      Author: Andy
 */
#include "vision.h"

int smallestBoxArea = 10000000; // arbitrary large number
int smallestBoxPV = 0; // calculated input for smallest box

namespace Vision
{

void initUI()
{
	namedWindow("main");
	//namedWindow("yellow");
	//namedWindow("grey");
}

void displayMat(Mat matToDisplay, std::string windowName)
{
	imshow(windowName, matToDisplay);
}

void detectAndDisplay(Mat image)
{
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

	if (!runHeadless)
	{
		displayMat(imageCopy, "main");
		//displayMat(imageCopyGrey, "grey");
		//displayMat(imageCopyYellow, "yellow");
	}

}

void iterateAndDrawContours(std::vector<vector<Point> > contours, Mat target)
{
	vector<vector<Point> >::iterator contour;

	for (contour = contours.begin(); contour < contours.end(); contour++)
	{
		Rect bound = boundingRect(Mat(*contour));

		float aspectRatio = (float) bound.width / (float) bound.height;
		int minimumActualArea = (target.rows * target.cols) * minimumArea;
		int maximumActualArea = (target.rows * target.cols) * maximumArea;

		if (aspectRatio < 1)
		{
			aspectRatio = 1.0f / aspectRatio;
		}

		//std::printf("%f\n", aspectRatio);

		if (bound.area() > minimumActualArea
				&& bound.area() <= maximumActualArea
				&& maxAspectRatio > aspectRatio)
		{

			rectangle(target, bound.tl(), bound.br(), Scalar(255, 255, 255));

			if (bound.area() < smallestBoxArea)
			{
				smallestBoxPV = (double) bound.x + ((double) bound.width / 2);
				smallestBoxArea = bound.area();
			}
		}
	}
}

int getInput() {
	return smallestBoxPV;
}

}
