//Dependencies

//OpenCV 2.4.10
//boost_system
//pthread

#include <thread>
#include <chrono>
#include <iostream>
#include <cstdio>

#include "globals.h"
#include "vision.h"
#include "pid.h"
#include "server.h"

void wait(int ms);
double normalize(double number);

int main() {

	initializeConfiguration();

	if (!runHeadless) {
		std::printf("Running with GUI\n");
		Vision::initUI();
	} else {
		std::printf("Running headless\n");
	}

	std::thread server(Server::start); // start udp server that serves rotational scalars

	Mat image;
	VideoCapture capture = VideoCapture(0);
	capture.set(CV_CAP_PROP_FRAME_WIDTH, resizeWidth);
	capture.set(CV_CAP_PROP_FRAME_HEIGHT, resizeHeight);
	capture >> image;

	PID pid(0.5, 0, 0, 0, 0, 0, 0);
	pid.SetSetpoint(((double) image.cols) / 2.0f);

	if (!capture.isOpened()) {
		std::cout << "Did not connect to camera.";
	} else {
		while (true) {
			if (capture.read(image)) {
				if (!runHeadless) {
					Vision::detectAndDisplay(image);
					waitKey(loopDelta); // use waitKey from OpenCV to delay if GUI is present
				} else {
					std::thread t1(Vision::detectAndDisplay, image);
					std::thread t2(wait, loopDelta); //if not use blocking thread function

					t2.join();
					t1.join();
				}

				//TODO: Implement proper PID
				pid.SetInput(Vision::getInput());
				double newThrottle = -1 * (pid.Update(loopDelta)); // positive value: turn right
				newThrottle = normalize(newThrottle);

				Server::updateThrottle(newThrottle);
			}
		}

	}

	capture.release();

	return 0;
}

void wait(int ms) {
	std::this_thread::sleep_for(std::chrono::milliseconds(ms));
}

double normalize(double number) {
	number = number * conversionFactor;

	if (number > 1.0f) {
		return 1.0f;
	}

	if (number < -1.0f) {
		return -1.0f;
	}

	return number;
}
