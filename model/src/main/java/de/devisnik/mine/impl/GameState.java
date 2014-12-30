package de.devisnik.mine.impl;

public class GameState {

	private final BoardState itsBoardState;
	private final int itsBombCount;
	private final boolean itsMined;
	private final int itsWatchState;

	GameState(Game game) {
		itsBoardState = ((Board) game.getBoard()).getState();
		itsBombCount = game.getBombCount();
		itsMined = game.isMined();
		itsWatchState = game.getWatch().getTime();
	}
	
	GameState(BoardState boardState, int bombsCount, boolean isMined, int watchState) {
		itsBoardState = boardState;
		itsBombCount = bombsCount;
		itsMined = isMined;
		itsWatchState = watchState;
	}

	public int getWatchState() {
		return itsWatchState;
	}
	
	public BoardState getBoardState() {
		return itsBoardState;
	}

	public int getBombsCount() {
		return itsBombCount;
	}

	public boolean isMined() {
		return itsMined;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((itsBoardState == null) ? 0 : itsBoardState.hashCode());
		result = prime * result + itsBombCount;
		result = prime * result + itsWatchState;
		result = prime * result + (itsMined ? 1231 : 1237);
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GameState))
			return false;
		GameState other = (GameState) obj;
		if (itsBoardState == null) {
			if (other.itsBoardState != null)
				return false;
		} else if (!itsBoardState.equals(other.itsBoardState))
			return false;
		if (itsBombCount != other.itsBombCount)
			return false;
		if (itsMined != other.itsMined)
			return false;
		if (itsWatchState != other.itsWatchState)
			return false;
		return true;
	}
}
