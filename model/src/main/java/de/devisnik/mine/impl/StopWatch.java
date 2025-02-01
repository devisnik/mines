package de.devisnik.mine.impl;

import java.util.ArrayList;
import java.util.List;

import de.devisnik.mine.IStopWatch;
import de.devisnik.mine.IStopWatchListener;

public class StopWatch implements IStopWatch {

    private final List<IStopWatchListener> itsListeners = new ArrayList<IStopWatchListener>();
    private int itsTime;

    public StopWatch() {
        this(0);
    }

    public StopWatch(int initialTime) {
        itsTime = initialTime;
    }

    @Override
    public void addListener(IStopWatchListener listener) {
        if (!itsListeners.contains(listener))
            itsListeners.add(listener);
    }

    @Override
    public int getTime() {
        return itsTime;
    }

    @Override
    public void removeListener(IStopWatchListener listener) {
        itsListeners.remove(listener);
    }

    void tick() {
        itsTime++;
        for (IStopWatchListener listener : itsListeners)
            listener.onTimeChange(itsTime);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + itsTime;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StopWatch other = (StopWatch) obj;
        if (itsTime != other.itsTime)
            return false;
        return true;
    }

}
