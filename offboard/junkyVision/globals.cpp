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

//GreyToteThreshold
int greyHlow = 31;
int greySlow = 52;
int greyVlow = 56;

int greyHhigh = 150;
int greyShigh = 251;
int greyVhigh = 246;

//YellowToteThreshold
int yellowHlow = 95;
int yellowSlow = 0;
int yellowVlow = 170;

int yellowHhigh = 99;
int yellowShigh = 255;
int yellowVhigh = 255;

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

	// GreyToteThreshold
	greyHlow = pt.get<int>("GreyToteThreshold.hLow");
	greySlow = pt.get<int>("GreyToteThreshold.sLow");
	greyVlow = pt.get<int>("GreyToteThreshold.vLow");

	greyHhigh = pt.get<int>("GreyToteThreshold.hHigh");
	greyShigh = pt.get<int>("GreyToteThreshold.sHigh");
	greyVhigh = pt.get<int>("GreyToteThreshold.vHigh");

	// YellowToteThreshold
	yellowHlow = pt.get<int>("YellowToteThreshold.hLow");
	yellowSlow = pt.get<int>("YellowToteThreshold.sLow");
	yellowVlow = pt.get<int>("YellowToteThreshold.vLow");

	yellowHhigh = pt.get<int>("YellowToteThreshold.hHigh");
	yellowShigh = pt.get<int>("YellowToteThreshold.sHigh");
	yellowVhigh = pt.get<int>("YellowToteThreshold.vHigh");

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
