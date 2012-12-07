package de.devisnik.mine;

/**
 * 2-dimensional minesweeper board, consisting of fields.
 * 
 * @since 1.0
 */
public interface IBoard {

	Point getDimension();

	int getFlagCount();

	int getOpenCount();

	IField getField(int x, int y);

	Point getPosition(IField field);

	boolean isOverflagged();
}