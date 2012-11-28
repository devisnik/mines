package de.devisnik.mine.impl;

import java.util.ArrayList;
import java.util.List;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.IGame;
import de.devisnik.mine.IMinesGameListener;
import de.devisnik.mine.IStopWatch;
import de.devisnik.mine.Point;

public class Game implements IGame {

	private final Board itsBoard;
	private final int itsBombsCount;
	private boolean itsIsMined = false;

	private final List<IMinesGameListener> itsListeners = new ArrayList<IMinesGameListener>();
	private final IBoardMiner itsMiner;
	private final StopWatch itsStopWatch;

	public Game(final int dimX, final int dimY, final int bombs, final IBoardMiner miner) {
		this(new InitialGameState(dimX, dimY, bombs), miner);
	}

	private Game(final GameState state, final IBoardMiner miner) {
		itsMiner = miner;
		itsBoard = Board.createFromState(state.getBoardState());
		itsBombsCount = state.getBombsCount();
		itsIsMined = state.isMined();
		itsStopWatch = new StopWatch(state.getWatchState());
	}

	public void addListener(final IMinesGameListener listener) {
		itsListeners.add(listener);
	}

	private void fireBusted() {
		for (IMinesGameListener listener : itsListeners) {
			listener.onBusted();
		}
	}

	private void fireChange() {
		for (IMinesGameListener listener : itsListeners) {
			listener.onChange(itsBoard.getFlagCount(), getBombCount());
		}
	}

	private void fireDisarmed() {
		for (IMinesGameListener listener : itsListeners) {
			listener.onDisarmed();
		}
	}

	private void fireStart() {
		for (IMinesGameListener listener : itsListeners) {
			listener.onStart();
		}
	}

	public IBoard getBoard() {
		return itsBoard;
	}

	/**
	 * @return Returns the itsBombsCount.
	 */
	public int getBombCount() {
		return itsBombsCount;
	}

	private void handleGameDone() {
		if (itsBoard.isExploded()) {
			itsBoard.openAllFlaggedOrBomb();
			fireBusted();
		} else if (isDisarmed()) {
			fireDisarmed();
		}
	}

	private boolean isAllFlagsUsed() {
		return itsBoard.getFlagCount() >= getBombCount();
	}

	private boolean isDisarmed() {
		int touched = itsBoard.getOpenCount() + itsBoard.getFlagCount();
		Point dimension = itsBoard.getDimension();
		return touched == dimension.x * dimension.y;
	}

	private boolean isFinished() {
		return isDisarmed() || itsBoard.isExploded();
	}

	/**
	 * @return Returns the itsIsMined.
	 */
	boolean isMined() {
		return itsIsMined;
	}

	private void mine(final IField firstField) {
		itsMiner.mine(itsBoard, itsBombsCount, firstField);
		itsBoard.setNeighborBombNumbers();
		itsIsMined = true;
	}

	public void onRequestFlag(final IField field) {
		if (isFinished()) {
			return;
		}
		itsBoard.updateLastTouched(field);
		if (!field.isOpen()) {
			if (!isAllFlagsUsed() || field.isFlagged()) {
				toggleFlag(field);
				fireChange();
			}
		} else {
			openNeighborsIfFullyFlagged(field);
		}
		handleGameDone();
	}

	private void openNeighborsIfFullyFlagged(final IField field) {
		if (field.getNeighborBombs() == itsBoard.getNeighborFlags(field)) {
			openUnflaggedNeighbors(field);
			fireChange();
		}
	}

	public void onRequestOpen(final IField field) {
		if (isFinished()) {
			return;
		}
		// treat open on a flagged field as an unflag operation
		if (field.isFlagged()) {
			onRequestFlag(field);
			return;
		}
		itsBoard.updateLastTouched(field);
		if (!isMined()) {
			mine(field);
			fireStart();
		}
		if (field.isOpen()) {
			openNeighborsIfFullyFlagged(field);
		} else {
			open((Field)field);
			fireChange();
		}
		handleGameDone();
	}

	public boolean isStarted() {
		return isMined();
	}

	public boolean isRunning() {
		return isStarted() && !isFinished();
	}

	private void open(final Field field) {
		itsBoard.openWithNeighbors(field);
	}

	private void openUnflaggedNeighbors(final IField field) {
		IField[] neighbors = field.getNeighbors();
		for (int i = 0; i < neighbors.length; i++) {
			IField neighbor = neighbors[i];
			if (!neighbor.isFlagged()) {
				open((Field) neighbor);
			}
		}
	}

	public void removeListener(final IMinesGameListener listener) {
		itsListeners.remove(listener);
	}

	private void toggleFlag(final IField field) {
		itsBoard.toggleFlagged(field);
	}

	public String toString() {
		return itsBoard.toString();
	}

	public static Game createFromState(final GameState state, final IBoardMiner miner) {
		return new Game(state, miner);
	}

	public GameState getState() {
		return new GameState(this);
	}

	public IStopWatch getWatch() {
		return itsStopWatch;
	}

	public void tickWatch() {
		if (isRunning())
			itsStopWatch.tick();
	}
}
