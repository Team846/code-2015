package utils;
import java.util.Timer;
import java.util.TimerTask;

public class Watchdog extends Timer{

	int time;
	int startTime;
	int feedTime;
	int killTime;
	
	TimerTask feed;
	TimerTask kill;
	
	Timer timer = new Timer();
	
	public Watchdog(int time){
		super();
		this.time = time;
		
	}
	
	public static TimerTask feed(){
		System.out.println("Feed the dog...");
		//feed
		return feed();
	}
	
	public static TimerTask kill(){
		System.out.println("Killed watchdog.");
		//kill
		return kill();
	}

	{
	kill = Watchdog.kill();
	killTime = 5;
	timer.schedule(kill, killTime);
	
	feed = Watchdog.feed();
	startTime = 0;
	feedTime = 3; //supposed to feed every 3 milliseconds
	timer = new Timer();
	timer.schedule(feed, startTime, feedTime);
	}
	
}
