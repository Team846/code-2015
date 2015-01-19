package utils;
import java.util.Timer;
import java.util.TimerTask;

public class Watchdog extends Timer{

	static Timer timer;
	
	int time;
	static long killTime = 5;
	
	static TimerTask kill = Watchdog.kill();
	
	public Watchdog(int time){
		super();
		this.time = time;
		
	}
	
	public static void start(){
		timer = new Timer();
		timer.schedule(kill, killTime);
		
	}
	
	public static void feed(){
		//feed
		System.out.println("Fed the dog");
		timer.cancel();
		Watchdog.start();
		
	}
	
	public static TimerTask kill(){		
		//kill
		System.out.print("Killed watchdog.");
		return kill;
	}
}
