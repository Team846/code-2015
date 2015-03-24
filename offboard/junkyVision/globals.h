/*
 * config.h
 *
 *  Created on: Feb 21, 2015
 *      Author: Andy
 */

#ifndef GLOBALS_H_
#define GLOBALS_H_

void initializeConfiguration();

extern bool runHeadless;
extern int loopHz;
extern int loopDelta;

extern int hLow;
extern int sLow;
extern int vLow;


extern int hHigh;
extern int sHigh;
extern int vHigh;

extern int cameraIndex;
extern int resizeWidth;
extern int resizeHeight;

extern double conversionFactor;

extern float maxAspectRatio;
extern float minimumArea;
extern float maximumArea;

#endif /* GLOBALS_H_ */
