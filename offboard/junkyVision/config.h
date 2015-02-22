/*
 * config.h
 *
 *  Created on: Feb 21, 2015
 *      Author: Andy
 */

#ifndef CONFIG_H_
#define CONFIG_H_

const bool runHeadless = false;
const int loopHz = 25;
const int loopDelta = 1000 / loopHz;

const int hLow = 70;
const int hHigh = 120;
const int sLow = 0;
const int sHigh = 255;
const int vLow = 150;
const int vHigh = 255;

const double conversionFactor = 1.0f / 150.0f;

const float maxAspectRatio = 2.4f;
const float minimumArea = 0.2f;
const float maximumArea = 1.0f;

#endif /* CONFIG_H_ */
