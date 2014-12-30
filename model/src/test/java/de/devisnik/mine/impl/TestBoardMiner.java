/**
 * 
 */
package de.devisnik.mine.impl;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;

final class TestBoardMiner implements IBoardMiner {

	private final boolean[][] itsMines;

	public TestBoardMiner(boolean[][] mines) {
		itsMines = mines;
	}

	public void mine(IBoard board, int bombs, IField clickedField) {
		for (int i = 0; i < itsMines.length; i++) {
			for (int j = 0; j < itsMines[i].length; j++) {
				((Field) board.getField(i, j)).setBomb(itsMines[i][j]);
			}
		}
	}
}