package edu.wm.werewolf.model;

import java.util.Date;

public class Game {
	private boolean isRunning;
	private int dayNightFreq;
	private long timer;
	
	public Game(int dayNightFreq, long createdDate) {
		super();
		this.dayNightFreq = dayNightFreq;
		this.timer = createdDate;
	}
	public int getDayNightFreq() {
		return dayNightFreq;
	}
	public void setDayNightFreq(int dayNightFreq) {
		this.dayNightFreq = dayNightFreq;
	}
	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	public boolean isNight() {
		Date comp = new Date();
		if(((comp.getTime()-timer)/dayNightFreq)%2 == 0)
			return true;
		return false;
	}
	/**
	 * @return the timer
	 */
	public long getTimer() {
		return timer;
	}
	/**
	 * @param timer the timer to set
	 */
	public void setTimer(long timer) {
		this.timer = timer;
	}
}
