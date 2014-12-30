package de.devisnik.mine.impl;

import de.devisnik.mine.Point;
import junit.framework.TestCase;

public class SimpleBoardMinerTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testMine() {
		Board board = new Board(5, 5);
		int bombs = 10;
		new SimpleBoardMiner().mine(board, bombs, (Field) board.getField(1, 1));
		assertEquals(bombs, countBombs(board));
		assertEquals(false, board.getField(1, 1).isBomb());
	}

	public void testTooManyBombs() {
		Board board = new Board(5, 5);
		int bombs = 25;
		new SimpleBoardMiner().mine(board, bombs, (Field) board.getField(1, 1));
		assertEquals(24, countBombs(board));
		assertEquals(false, board.getField(1, 1).isBomb());
	}
	
	private int countBombs(Board board) {
		int counter = 0;
		Point dimension = board.getDimension();
		for (int dimX = 0; dimX < dimension.x; dimX++) {
			for (int dimY = 0; dimY < dimension.y; dimY++) {
				if (board.getField(dimX, dimY).isBomb())
					counter++;
			}
		}
		return counter;
	}

}
