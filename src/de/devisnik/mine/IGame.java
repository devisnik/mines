/*
 * Created on 27.01.2006 by Volker
 *
 */
package de.devisnik.mine;

/**
 * @since 1.0
 */
public interface IGame {

	void onRequestOpen(IField field);

	void onRequestFlag(IField field);

	void addListener(IMinesGameListener listener);

	void removeListener(IMinesGameListener listener);

	int getBombCount();

	IBoard getBoard();

	boolean isStarted();
	
	boolean isRunning();
	
	IStopWatch getWatch();
	
	void tickWatch();
}