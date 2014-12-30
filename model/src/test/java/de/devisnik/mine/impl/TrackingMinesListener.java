package de.devisnik.mine.impl;

import de.devisnik.mine.IMinesGameListener;

public class TrackingMinesListener implements IMinesGameListener {

	
	private int busted;
	private int change;
	private int disarmed;
	private int start;

	public int getBusted() {
		return busted;
	}

	public int getChange() {
		return change;
	}

	public int getDisarmed() {
		return disarmed;
	}

	public int getStart() {
		return start;
	}


	public void onBusted() {
		busted++;
	}

	public void onChange(int flags, int mines) {
		change++;
	}

	public void onDisarmed() {
		disarmed++;
	}

	public void onStart() {
		start++;
	}

}
