/*
 * globals.cpp
 *
 *  Created on: Mar 23, 2015
 *      Author: Andy
 */

#include "globals.h"

#include <iostream>
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/ini_parser.hpp>

//General
bool runHeadless = false;
int loopHz = 25;
int loopDelta = 1000 / loopHz;

//ColorThresholds
int hLow = 31;
int sLow = 52;
int vLow = 56;

int hHigh = 150;
int sHigh = 251;
int vHigh = 246;

//Image
int cameraIndex = 2;
int resizeWidth = 240;
int resizeHeight = 160;

//Recognition
float maxAspectRatio = 2.4f;
float minimumArea = 0.2f;
float maximumArea = 1.0f;

//PID
double conversionFactor = 1.0f / 150.0f;

void initializeConfiguration() {
	boost::property_tree::ptree pt;
	boost::property_tree::ini_parser::read_ini("config.ini", pt);

	// General
	runHeadless = pt.get<bool>("General.runHeadless");
	loopHz = pt.get<int>("General.loopHz");
	loopDelta = 1000 / loopHz;

	// ColorThresholds
	hLow = pt.get<int>("ColorThresholds.hLow");
	sLow = pt.get<int>("ColorThresholds.sLow");
	vLow = pt.get<int>("ColorThresholds.vLow");

	hHigh = pt.get<int>("ColorThresholds.hHigh");
	sHigh = pt.get<int>("ColorThresholds.sHigh");
	vHigh = pt.get<int>("ColorThresholds.vHigh");

	// Image
	cameraIndex = pt.get<int>("Image.cameraIndex");
	resizeWidth = pt.get<int>("Image.resizeWidth");
	resizeHeight = pt.get<int>("Image.resizeHeight");

	// Recognition
	maxAspectRatio = pt.get<float>("Recognition.maxAspectRatio");
	minimumArea = pt.get<float>("Recognition.minimumArea");
	maximumArea = pt.get<float>("Recognition.maximumArea");

	// PID
	conversionFactor = pt.get<float>("PID.conversionFactor");
}
