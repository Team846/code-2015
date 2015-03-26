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

extern int greyHlow;
extern int greySlow;
extern int greyVlow;

extern int greyHhigh;
extern int greyShigh;
extern int greyVhigh;

extern int yellowHlow;
extern int yellowSlow;
extern int yellowVlow;

extern int yellowHhigh;
extern int yellowShigh;
extern int yellowVhigh;

extern int cameraIndex;
extern int resizeWidth;
extern int resizeHeight;

extern double conversionFactor;

extern float maxAspectRatio;
extern float minimumArea;
extern float maximumArea;

#endif /* GLOBALS_H_ */
