package de.devisnik.mine;

import de.devisnik.mine.impl.ExcludeNeighborsBoardMiner;
import de.devisnik.mine.impl.Game;

/**
 * A GWT compatible GameFactory that does not handle persistence.
 */
public final class SimpleGameFactory {
    private static final ExcludeNeighborsBoardMiner BOARD_MINER = new ExcludeNeighborsBoardMiner();

    private SimpleGameFactory() {
    }

    public static IGame create(int width, int height, int bombs) {
        return new Game(width, height, bombs, BOARD_MINER);
    }
}
