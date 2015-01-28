package log;

import java.io.File;
import java.util.Queue;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

public class DashboardLogger
{
	private static DashboardLogger instance;
	
	private Configuration config;
	private SocketIOServer server;
	
	public DashboardLogger() {
		config = new Configuration();
		config.setHostname("localhost");
		config.setPort(8081);
		server = new SocketIOServer(config);
	}
	
	public void log(DashboardLog message) {
		System.out.println("logging");
		server.getBroadcastOperations().sendEvent("data-update", message.json());
	}
	
	public static DashboardLogger getInstance() {
		if (instance == null) {
			instance = new DashboardLogger();
		}
		
		return instance;
	}
	
	public void tick() {
		System.out.println("tick");
		server.getBroadcastOperations().sendEvent("foo", "hi");
	}
	
	static int countdown = 10;
	
	public static void main(String[] args) {
		while (true) {
			DashboardLogger.getInstance().log(new IntegerLog("motor-speed", (int) (Math.random() * 5)));
			
			countdown--;
			if (countdown == 0) {
				DashboardLogger.getInstance().log(new BooleanLog("robot-on", (Math.random() * 5) <= 2));
				countdown = 10;
			}
			
			
			try
			{
				Thread.sleep(50);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
