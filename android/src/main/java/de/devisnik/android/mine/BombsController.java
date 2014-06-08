package de.devisnik.android.mine;

import de.devisnik.mine.IGame;
import de.devisnik.mine.MinesGameAdapter;

public class BombsController extends MinesGameAdapter {

	private final IGame itsGame;
	private final CounterView itsCounter;

	public BombsController(IGame game, final CounterView counter) {
		this.itsGame = game;
		itsCounter = counter;
		itsCounter.setValue(game.getBombCount()-game.getBoard().getFlagCount());
		game.addListener(this);
	}
	
	public void dispose() {
		itsGame.removeListener(this);
	}
	
	@Override
	public void onChange(int flags, int mines) {
		itsCounter.setValue(mines - flags);
	}
}
