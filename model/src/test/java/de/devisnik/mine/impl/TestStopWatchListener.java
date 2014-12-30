package de.devisnik.mine.impl;

import de.devisnik.mine.IStopWatchListener;

final class TestStopWatchListener implements IStopWatchListener {

	int itsCounter = 0;

	@Override
	public void onTimeChange(int currentTime) {
		itsCounter++;
	}
}