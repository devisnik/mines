package de.devisnik.android.mine;

import de.devisnik.mine.IGame;
import de.devisnik.mine.IStopWatch;
import de.devisnik.mine.IStopWatchListener;

public class TimerController {

	private final CounterView itsCounter;
	private IStopWatchListener itsListener;
	private final IStopWatch itsWatch;

	public TimerController(IGame game, CounterView counter) {
		itsWatch = game.getWatch();
		this.itsCounter = counter;
		itsCounter.setValue(itsWatch.getTime());
		itsListener = new IStopWatchListener() {
			@Override
			public void onTimeChange(int time) {
				itsCounter.setValue(itsWatch.getTime());
			}
		};
		itsWatch.addListener(itsListener);
	}

	public void dispose() {
		itsWatch.removeListener(itsListener);
	}
}
