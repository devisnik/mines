package de.devisnik.mine.impl;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;

/**
 * Interface for laying out mines into a board.
 */
public interface IBoardMiner {

	/**
	 * Lay out the mines, considering the given field as clicked.
	 * 
	 * @param board
	 *            the board to layout mines into
	 * @param bombs
	 *            number of mines to lay out
	 * @param clickedField
	 *            the field selected (should usually not become mined).
	 */
	void mine(IBoard board, int bombs, IField clickedField);
}