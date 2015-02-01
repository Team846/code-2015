package sensors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OpticalFlow {

	public static final int COMPONENT_X = 0;
	public static final int COMPONENT_Y = 1;

	private static String mousePathPrefix = "/dev/input/mouse";

	private static class MouseInterruptThread implements Runnable {
		private Thread t;
		private String mousePath;
		
		int position[];
		int delta[];

		byte[] mouseData;

		private FileInputStream mouse;

		public MouseInterruptThread(String path, OpticalFlow sensor)
				throws FileNotFoundException {
			mousePath = path;
			
			mouseData = new byte[3];
			
			position = new int[2];
			position[COMPONENT_X] = 0;
			position[COMPONENT_Y] = 0;

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

						System.out.println("WARNING: Maximum logical range reached! Position may not be accurate. Use a higher DPI setting or a better mouse.");
					}

					position[COMPONENT_X] += delta[COMPONENT_X];
					position[COMPONENT_Y] += delta[COMPONENT_Y];

				} catch (IOException e) {
					// TODO Auto-generated catch block
					// System.out.println("shit happend bro");
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

	MouseInterruptThread mouseThread;
	
	public int IPS = 15;

	public int getIPS() {
		return IPS;
	}

	public void setIPS(int iPS) {
		IPS = iPS;
	}

	public OpticalFlow(int mouseIndex) {
		try {
			mouseThread = new MouseInterruptThread(mousePathPrefix + mouseIndex,
					this);
			mouseThread.start();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private double convertMouseUnitToInch(int mouseUnits) {
		/*
		 * IPS should be defined as the maximum speed the mouse is capable of (aka
		 * when mouse reads max. value)
		 * 
		 * This means maximum speed == 127. Deriving from this :
		 * 
		 * inches = (IPS * mouse units)/127
		 * 
		 */

		return (IPS * mouseUnits) / 127;
	}

	public double getCurrentPosition(int componentType) {
		return convertMouseUnitToInch(mouseThread.getPosition()[componentType]);
	}

	public double getLastDelta(int componentType) {
		return convertMouseUnitToInch(mouseThread.getDelta()[componentType]);
	}
}