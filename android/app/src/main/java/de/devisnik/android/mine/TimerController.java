package de.devisnik.android.mine;

import de.devisnik.mine.IGame;
import de.devisnik.mine.IStopWatch;
import de.devisnik.mine.IStopWatchListener;

public class TimerController {

    private final CounterView counterView;
    private final IStopWatchListener stopWatchListener;
    private final IStopWatch stopWatch;

    public TimerController(IGame game, CounterView counter) {
        stopWatch = game.getWatch();
        this.counterView = counter;
        counterView.setValue(stopWatch.getTime());
        stopWatchListener = time -> counterView.setValue(stopWatch.getTime());
        stopWatch.addListener(stopWatchListener);
    }

    public void dispose() {
        stopWatch.removeListener(stopWatchListener);
    }
}
