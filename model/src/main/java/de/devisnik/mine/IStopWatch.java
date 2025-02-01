package de.devisnik.mine;

/**
 * @since 1.0
 */
public interface IStopWatch {
    
    int getTime();
    void addListener(IStopWatchListener listener);
    void removeListener(IStopWatchListener listener);
}
