//Dependencies

//OpenCV 2.4.10
//Boost ASIO
//pthread

#include <thread>
#include <chrono>
#include <iostream>
#include <cstdio>

#include "vision.h"
#include "pid.h"
#include "server.h"

void wait(int ms);
double normalize(double number);

int main(int argc, char** argv) {
	if (!runHeadless) {
		std::cout << "Running with GUI" << std::endl;
		Vision::initUI();
	} else {
		std::cout << "Running headless" << std::endl;
	}

	std::thread server(Server::start); // start udp server that serves rotational scalars

	Mat image;
	VideoCapture capture = VideoCapture(0);
	capture.read(image);

	PID pid(0.5, 0, 0, 0, 0, 0, 0);
	pid.SetSetpoint(((double) image.cols) / 2.0f);

	if (!capture.isOpened()) {
		std::cout << "Did not connect to camera.";
	} else {
		while (true) {
			capture.read(image);

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

	capture.release();

	return 0;
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
