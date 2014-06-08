package de.devisnik.mine.robot;

import de.devisnik.mine.IGame;


public class AutoPlayer {

	private final IGame itsGame;
	private final boolean itsGuess;

	public AutoPlayer(final IGame game, boolean guess) {
		this.itsGame = game;
		this.itsGuess = guess;
	}

	public void doNextMove() {
		// do internal move until successful execution
		while (!internalNextMove()) {
		}
	}

	private boolean internalNextMove() {
		GameResolver gameResolver = new GameResolver(itsGame);
		if (gameResolver.isEmptyCommandStack()) {
			fillCommandStack(gameResolver);
		}
		//still empty --> no more moves, done?
		if (gameResolver.isEmptyCommandStack()) {
			return true;
		}
		return ((IMove) gameResolver.getCommandStack().pop()).execute();
	}

	
	private void fillCommandStack(GameResolver resolver) {
		if (resolver.findUniqueResolution()) {
			return;
		}
		if (resolver.applyFirstResolution()) {
			System.out.println("guessing");
			return;
		}
		resolver.openFirstClosedUnflaggedField();
		System.out.println("open random");
	}


}
