package environment;

public class Timer {

	private int minutes;
	private int seconds;

	public Timer(int minutes, int seconds) {
		this.setMinutes(minutes);
		this.setSeconds(seconds);
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	public void startTimer() {
		
	}
	
	public void stopTimer() {
		
	}
	
	public void addSeconds(int timeInSeconds) {
		int newSeconds = Math.floorMod(timeInSeconds + getSeconds(), 60);
		int minutesToAdd = Math.floorDiv(timeInSeconds + getSeconds(), 60);
		setSeconds(newSeconds);
		setMinutes(getMinutes() + minutesToAdd);
	}
}
