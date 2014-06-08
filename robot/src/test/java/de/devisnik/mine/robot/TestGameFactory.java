package de.devisnik.mine.robot;

import de.devisnik.mine.IGame;
import de.devisnik.mine.impl.Game;
import de.devisnik.mine.impl.TestBoardMiner;

public class TestGameFactory {

	public static IGame create3by3_1() {
		boolean[][] mines = new boolean[][] { { false, false, true },
				{ false, false, false }, { true, false, false } };

		return createGame(mines);
	}

	public static IGame create3by3_2() {
		boolean[][] mines = new boolean[][] { { false, false, false },
				{ false, false, true }, { false, true, false } };
		return createGame(mines);
	}

	public static IGame create3by3_3() {
		boolean[][] mines = new boolean[][] { { false, false, false },
				{ false, false, false }, { false, true, true } };
		return createGame(mines);
	}
	
	public static IGame create3by3_4() {
		boolean[][] mines = new boolean[][] { { false, false, false },
				{ false, false, false }, { false, true, false } };
		return createGame(mines);
	}
	
	public static IGame create3by3_5() {
		boolean[][] mines = new boolean[][] { { false, false, false },
				{ false, false, false }, { true, false, true } };
		return createGame(mines);
	}
	
	public static IGame create3by2_1() {
		boolean[][] mines = new boolean[][] { { false, true },
				{ false, true }, { false, true} };
		return createGame(mines);
	}

	private static IGame createGame(boolean[][] mines) {
		return new Game(mines.length, mines[0].length, countMines(mines), new TestBoardMiner(mines));
	}
	
	private static int countMines(boolean[][] mines) {
		int counter = 0;
		for (int i = 0; i < mines.length; i++)
			for (int j = 0; j < mines[i].length; j++)
				if (mines[i][j]) counter++;
		return counter;
	}
}
