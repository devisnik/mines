package de.devisnik.mine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.devisnik.mine.impl.ExcludeNeighborsBoardMiner;
import de.devisnik.mine.impl.Game;
import de.devisnik.mine.impl.GameState;
import de.devisnik.mine.impl.StatePersistence;

/**
 * Factory to create, persist, clone and restore game objects.
 * 
 * @since 1.0
 */
public final class GameFactory {

	private static ExcludeNeighborsBoardMiner BOARDMINER = new ExcludeNeighborsBoardMiner();

	private GameFactory() {
	}

	public static IGame create(int width, int height, int bombs) {
		return new Game(width, height, bombs, BOARDMINER);
	}

	public static void writeToStream(IGame game, OutputStream stream) throws IOException {
		GameState state = ((Game) game).getState();
		StatePersistence.writeGameState(state, stream);
	}

	public static IGame readFromStream(InputStream stream) throws IOException {
		GameState state = StatePersistence.readGameState(stream);
		return Game.createFromState(state, BOARDMINER);
	}

	public static IGame clone(IGame game) {
		GameState state = ((Game) game).getState();
		return Game.createFromState(state, BOARDMINER);
	}
}
