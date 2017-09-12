package webprowler.utility;

import java.util.Timer;
import java.util.TimerTask;

public class ProwlTimer {
	private int secondsPassed = 0;
	private int setTime;
	
	private Timer timer = new Timer();
	private TimerTask task = new TimerTask() {
		public void run() {
			secondsPassed = secondsPassed + 1;
			setTime = setTime + 1;
		}
	};
	
	public ProwlTimer(int setTime) 
	{
		this.setTime = setTime;
		timer.scheduleAtFixedRate(task,1000,1000);
	}
	
	public int getTime() { return setTime; }
	public void resetTimer(int time) { setTime = time; }
}