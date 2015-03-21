package com.team846.frc2015.sensors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.team846.frc2015.utils.AsyncPrinter;

class OpticalFlow {

	private static final int COMPONENT_X = 0;
	private static final int COMPONENT_Y = 1;

	private static String mousePathPrefix = "/dev/input/mouse";
	
	private static void log(Object msg) {
		AsyncPrinter.info(msg.toString());
	}

	private static class MouseInterruptThread implements Runnable {
		private Thread t;
		private String mousePath;
		
		int position[];
		int delta[];

		byte[] mouseData;

		private FileInputStream mouse;
		
		public void zeroPosition() {
			position[COMPONENT_X] = 0;
			position[COMPONENT_Y] = 0;
		}

		public MouseInterruptThread(String path, OpticalFlow sensor)
				throws FileNotFoundException {
			mousePath = path;
			
			mouseData = new byte[3];
			
			position = new int[2];
			zeroPosition();

			delta = new int[2];
			delta[COMPONENT_X] = 0;
			delta[COMPONENT_Y] = 0;

			mouse = new FileInputStream(mousePath);
		}

		public void run() {
			while (true) {
				try {

					mouse.read(mouseData); // BLOCKING CODE

					// [buttons, delta x, delta y]
					delta[COMPONENT_X] = mouseData[1];
					delta[COMPONENT_Y] = mouseData[2];

					if (Math.abs(delta[COMPONENT_X]) == 127
							|| Math.abs(delta[COMPONENT_Y]) == 127) {

						log("WARNING: Maximum logical range reached! Position may not be accurate. Use a higher DPI setting or a better mouse.");
					}

					position[COMPONENT_X] += delta[COMPONENT_X];
					position[COMPONENT_Y] += delta[COMPONENT_Y];

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public int[] getPosition() {
			return position;
		}

		public int[] getDelta() {
			return delta;
		}

		public void start() {
			if (t == null) {
				t = new Thread(this, mousePath); // using mouse path as thread
													// name
				t.start();
			}
		}
	}

	private MouseInterruptThread mouseThread;

	public OpticalFlow(int mouseIndex) {
		try {
			mouseThread = new MouseInterruptThread(mousePathPrefix + mouseIndex,
					this);
			mouseThread.start();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private int DPI = 800;
	
	private double convertMouseUnitToInch(int mouseUnits) {
		return mouseUnits / DPI;
	}
	
	public int getDPI() {
		return DPI;
	}

	public void setDPI(int dPI) {
		DPI = dPI;
	}
	
	public void zeroPosition() {
		mouseThread.zeroPosition();
	}

	public double getCurrentPosition(int componentType) {
		return convertMouseUnitToInch(mouseThread.getPosition()[componentType]);
	}

	public double getLastDelta(int componentType) {
		return convertMouseUnitToInch(mouseThread.getDelta()[componentType]);
	}
}