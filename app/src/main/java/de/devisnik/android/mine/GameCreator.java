package de.devisnik.android.mine;

import de.devisnik.mine.GameFactory;
import de.devisnik.mine.IGame;

public class GameCreator {

	private final Settings itsSettings;

	public GameCreator(Settings configuration) {
		itsSettings = configuration;
	}

	public IGame create() {
		int[] dimension = itsSettings.getBoardDimension();
		int width = dimension[0];
		int height = dimension[1];
		int bombs = itsSettings.getBombsNumber(width * height);
		return GameFactory.create(width, height, bombs);
	}

}
