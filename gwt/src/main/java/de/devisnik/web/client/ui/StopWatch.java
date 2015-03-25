package de.devisnik.web.client.ui;

import com.google.gwt.user.client.Timer;

public abstract class StopWatch {

	private boolean itsIsRunning;
	private Timer itsTimer;

	public StopWatch() {
		setTime(0);
	}
	
	public void start() {
		if (itsIsRunning) {
			return;
		}
		itsTimer = new Timer() {
			private int seconds = 0;
			public void run() {
				seconds++;
				setTime(seconds);
			}
		};
		itsTimer.scheduleRepeating(1000);
		itsIsRunning = true;
	}
	
	public void stop() {
		if (!itsIsRunning) {
			return;
		}
		itsTimer.cancel();
		itsIsRunning = false;
	}
	
	public void reset() {
		stop();
		setTime(0);
	}
	
	public boolean isRunning() {
		return itsIsRunning;
	}
	
	public abstract void setTime(int value);
}
