package dashboard;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.io.File;
import java.util.Queue;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOChannelInitializer;
import com.corundumstudio.socketio.SocketIOServer;

import config.DriverStationConfig;
import driverstation.LRTDriverStation;
import driverstation.LRTJoystick;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;

public class DashboardLogger
{
	private static DashboardLogger instance;

	private Configuration config;
	private SocketIOServer server;

	// Use on prod
	private boolean buttonsPressed() {
		LRTJoystick driverStick = LRTDriverStation.Instance().GetDriverStick();
		
		return driverStick.IsButtonDown(DriverStationConfig.JoystickButtons.DASHBOARD_ENABLE1) && 
				driverStick.IsButtonDown(DriverStationConfig.JoystickButtons.DASHBOARD_ENABLE2);
	}
	
	// Use on dev
//	private boolean buttonsPressed() {
//		return true;
//	}
	
	public DashboardLogger()
	{
		if (buttonsPressed())
		{
			System.out.println("starting funkyDashboard");
			config = new Configuration();
			config.setPort(8080);
			config.getSocketConfig().setReuseAddress(true);
			server = new SocketIOServer(config);

			server.setPipelineFactory(new StaticSocketIOChannel(config));
			server.start();
		}
	}

	public void log(DashboardLog message)
	{
		if (server != null) {
			server.getBroadcastOperations().sendEvent("data-update", message.json());
		}
	}

	public static DashboardLogger getInstance()
	{
		if (instance == null)
		{
			instance = new DashboardLogger();
		}

		return instance;
	}

	public void tick()
	{
		log(new BooleanLog("robot-on", DriverStation.getInstance().isEnabled()));
		log(new DoubleLog("motor-speed", (Math.random() * 6)));
	}

	class StaticSocketIOChannel extends SocketIOChannelInitializer
	{
		Configuration configuration;

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

	// Comment out while building, it may get used as an entrypoint
	public static void main(String[] args)
	{
		while (true)
		{
			getInstance().log(new DoubleLog("motor-speed", Math.random() * 5));
			try
			{
				Thread.sleep(50);
			} catch (InterruptedException e) {}
		}
	}
}
