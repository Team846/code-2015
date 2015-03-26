/*
 * vision.h
 *
 *  Created on: Feb 21, 2015
 *      Author: Andy
 */

#ifndef VISION_H_
#define VISION_H_

#include "opencv2/opencv.hpp"
#include <vector>

#include "globals.h"

using namespace cv;

namespace Vision {

void initUI();
void displayMat(Mat matToDisplay, std::string windowName);
void detectAndDisplay(Mat image);
void iterateAndDrawContours(std::vector<std::vector<Point> > contours, Mat target, Scalar color);
int getStrafePV();
int getForwardPV();

}

#endif /* VISION_H_ */
