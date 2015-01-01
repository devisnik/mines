package de.devisnik.android.mine;

import de.devisnik.mine.GameFactory;
import de.devisnik.mine.IGame;

public class GameCreator {

	private final Settings settings;

	public GameCreator(Settings settings) {
		this.settings = settings;
	}

	public IGame create() {
		int[] dimension = settings.getBoardDimension();
		int width = dimension[0];
		int height = dimension[1];
		int bombs = settings.getBombsNumber(width * height);
		return GameFactory.create(width, height, bombs);
	}

}
