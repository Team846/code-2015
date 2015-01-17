package utils;

public class Watchdog {
	
	int time = 5; //milliseconds
	
	public Watchdog(int time){
		super();
		this.time = time;
	}
	
	public boolean feed(){
		
		int time = 5; //resets time
		
		boolean previous = true; //gets previous state before feeding dog
		System.out.println("Feed the dog..."); //feed
		return previous;
	}
	
	public void kill(){
		System.out.println("Killed watchdog.");
		//kill
	}

}
