package com.team846.frc2015.dashboard;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOChannelInitializer;
import com.corundumstudio.socketio.SocketIOServer;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.LRTDriverStation;
import com.team846.frc2015.driverstation.LRTJoystick;
import com.team846.frc2015.logging.AsyncLogger;

public class DashboardLogger
{
	private static DashboardLogger instance;

	private Configuration config;
	private SocketIOServer server;

	public static void Initialize()
	{
		if (instance == null)
			instance = new DashboardLogger();
	}

	public static DashboardLogger getInstance()
	{
		if (instance == null)
			instance = new DashboardLogger();
		return instance;
	}

	private DashboardLogger()
	{
		if (true)
		{
			AsyncLogger.info("Starting funkyDashboard...");
			config = new Configuration();
			config.setPort(8080);
			config.getSocketConfig().setReuseAddress(true);
			server = new SocketIOServer(config);

			server.setPipelineFactory(new StaticSocketIOChannel(config));
			server.start();
		}
	}

	// Use on prod
	private boolean buttonsPressed() {
		LRTJoystick driverWheel = LRTDriverStation.instance().getDriverWheel();

		return driverWheel.isButtonDown(DriverStationConfig.JoystickButtons.DASHBOARD_ENABLE1) &&
				driverWheel.isButtonDown(DriverStationConfig.JoystickButtons.DASHBOARD_ENABLE2);
	}

	@SuppressWarnings("rawtypes")
	public void logString(String id, String data)
	{
		if (server != null) {
			server.getBroadcastOperations().sendEvent("data-update", "{ \"type\": \"" + id + "\", \"value\": \"" + data + "\" }");
		}
	}

	@SuppressWarnings("rawtypes")
	public void logInt(String id, Integer data)
	{
		if (server != null) {
			server.getBroadcastOperations().sendEvent("data-update", "{ \"type\": \"" + id + "\", \"value\": \"" + data + "\" }");
		}
	}

	@SuppressWarnings("rawtypes")
	public void logDouble(String id, Double data)
	{
		if (server != null) {
			server.getBroadcastOperations().sendEvent("data-update", "{ \"type\": \"" + id + "\", \"value\": \"" + data + "\" }");
		}
	}

	@SuppressWarnings("rawtypes")
	public void logFloat(String id, Float data)
	{
		if (server != null) {
			server.getBroadcastOperations().sendEvent("data-update", "{ \"type\": \"" + id + "\", \"value\": \"" + data + "\" }");
		}
	}

	@SuppressWarnings("rawtypes")
	public void logBoolean(String id, Boolean data)
	{
		if (server != null) {
			server.getBroadcastOperations().sendEvent("data-update", "{ \"type\": \"" + id + "\", \"value\": \"" + data + "\" }");
		}
	}

	@SuppressWarnings("rawtypes")
	public void log(String id, String data)
	{
		if (server != null) {
			server.getBroadcastOperations().sendEvent("data-update", "{ \"type\": \"" + id + "\", \"value\": " + data + " }");
		}
	}

	class StaticSocketIOChannel extends SocketIOChannelInitializer
	{
		final Configuration configuration;

		public StaticSocketIOChannel(Configuration configuration)
		{
			super();
			this.configuration = configuration;
		}

		@Override
		protected void initChannel(Channel ch) throws Exception
		{
			ChannelPipeline pipeline = ch.pipeline();
			addSslHandler(pipeline);
			addSocketioHandlers(pipeline);
			pipeline.addAfter(HTTP_ENCODER, "static", new HttpStaticFileServerHandler());
		}
	}

//	// Comment out while building, it may get used as an entrypoint
//	public static void main(String[] args)
//	{
//		while (true)
//		{
//			getInstance().log(new DoubleLog("motor-speed", Math.random() * 5));
//			Long curTime = System.currentTimeMillis();
//			getInstance().log(new StringLog("my-fancy-logs", curTime.toString()));
//			getInstance().log(new StringLog("my-fancy-strings", curTime.toString()));
//			try
//			{
//				Thread.sleep(50);
//			} catch (InterruptedException e) {}
//		}
//	}
}
