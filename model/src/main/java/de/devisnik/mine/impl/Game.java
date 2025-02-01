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

    @Override
    public void addListener(final IMinesGameListener listener) {
        itsListeners.add(listener);
    }

    private void fireBusted() {
        for (IMinesGameListener listener : itsListeners)
            listener.onBusted();
    }

    private void fireChange() {
        for (IMinesGameListener listener : itsListeners)
            listener.onChange(itsBoard.getFlagCount(), getBombCount());
    }

    private void fireDisarmed() {
        for (IMinesGameListener listener : itsListeners)
            listener.onDisarmed();
    }

    private void fireStart() {
        for (IMinesGameListener listener : itsListeners)
            listener.onStart();
    }

    private void fireClickedAfterFinished() {
        for (IMinesGameListener listener : itsListeners)
            listener.onClickAfterFinished();
    }

    @Override
    public IBoard getBoard() {
        return itsBoard;
    }

    /**
     * @return Returns the itsBombsCount.
     */
    @Override
    public int getBombCount() {
        return itsBombsCount;
    }

    private void handleGameDone() {
        if (itsBoard.isExploded()) {
            itsBoard.openAllFlaggedOrBomb();
            fireBusted();
        } else if (isDisarmed())
            fireDisarmed();
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

    boolean isMined() {
        return itsIsMined;
    }

    private void mine(final Field firstField) {
        itsMiner.mine(itsBoard, itsBombsCount, firstField);
        itsBoard.setNeighborBombNumbers();
        itsIsMined = true;
    }

    @Override
    public void onRequestFlag(final IField fieldInterface) {
        if (!isMined()) {
            onRequestOpen(fieldInterface);
            return;
        }
        Field field = (Field) fieldInterface;
        if (isFinished()) {
            fireClickedAfterFinished();
            return;
        }
        itsBoard.updateLastTouched(field);
        if (!field.isOpen()) {
            if (!isAllFlagsUsed() || field.isFlagged()) {
                toggleFlag(field);
                fireChange();
            }
        } else
            openNeighborsIfFullyFlagged(field);
        handleGameDone();
    }

    private void openNeighborsIfFullyFlagged(final Field field) {
        if (field.getNeighborBombs() == itsBoard.getNeighborFlags(field)) {
            openUnflaggedNeighbors(field);
            fireChange();
        }
    }

    @Override
    public void onRequestOpen(final IField fieldInterface) {
        Field field = (Field) fieldInterface;
        if (isFinished() || (field.getNeighborBombs() == 0 && field.isOpen())) {
            fireClickedAfterFinished();
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
        if (field.isOpen())
            openNeighborsIfFullyFlagged(field);
        else {
            open(field);
            fireChange();
        }
        handleGameDone();
    }

    @Override
    public boolean isStarted() {
        return isMined();
    }

    @Override
    public boolean isRunning() {
        return isStarted() && !isFinished();
    }

    private void open(final Field field) {
        itsBoard.openWithNeighbors(field);
    }

    private void openUnflaggedNeighbors(final Field field) {
        Field[] neighbors = field.getNeighbors();
        for (int i = 0; i < neighbors.length; i++) {
            Field neighbor = neighbors[i];
            if (!neighbor.isFlagged())
                open(neighbor);
        }
    }

    @Override
    public void removeListener(final IMinesGameListener listener) {
        itsListeners.remove(listener);
    }

    private void toggleFlag(final Field field) {
        itsBoard.toggleFlagged(field);
    }

    @Override
    public String toString() {
        return itsBoard.toString();
    }

    public static Game createFromState(final GameState state, final IBoardMiner miner) {
        return new Game(state, miner);
    }

    public GameState getState() {
        return new GameState(this);
    }

    @Override
    public IStopWatch getWatch() {
        return itsStopWatch;
    }

    @Override
    public void tickWatch() {
        if (isRunning())
            itsStopWatch.tick();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (itsBoard == null ? 0 : itsBoard.hashCode());
        result = prime * result + itsBombsCount;
        result = prime * result + (itsIsMined ? 1231 : 1237);
        result = prime * result + (itsStopWatch == null ? 0 : itsStopWatch.hashCode());
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
        Game other = (Game) obj;
        if (itsBoard == null) {
            if (other.itsBoard != null)
                return false;
        } else if (!itsBoard.equals(other.itsBoard))
            return false;
        if (itsBombsCount != other.itsBombsCount)
            return false;
        if (itsIsMined != other.itsIsMined)
            return false;
        if (itsStopWatch == null) {
            if (other.itsStopWatch != null)
                return false;
        } else if (!itsStopWatch.equals(other.itsStopWatch))
            return false;
        return true;
    }
}
