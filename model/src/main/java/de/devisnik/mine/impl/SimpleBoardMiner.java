package de.devisnik.mine.impl;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.Point;

public class SimpleBoardMiner implements IBoardMiner {

	@Override
	public void mine(IBoard board, int bombs, IField firstField) {
		Point dimension = board.getDimension();
		if (bombs >= dimension.x * dimension.y - 1)
			setAllOthersBombs(board, (Field) firstField, dimension);
		else
			setBombsRandomly(board, bombs, (Field) firstField, dimension);
	}

	private void setBombsRandomly(IBoard board, int bombs, Field excludeField,
			Point dimension) {
		int count = bombs;
		while (count > 0) {
			final int pos = (int) (Math.random() * dimension.x * dimension.y);
			final int px = pos % dimension.x;
			final int py = pos / dimension.x;
			IField field = board.getField(px, py);
			if (excludeField != field)
				if (setBombAt((Field) field))
					count--;
		}
	}

	private void setAllOthersBombs(IBoard board, Field excludeField,
			Point dimension) {
		for (int i = 0; i < dimension.y; i++)
			for (int j = 0; j < dimension.x; j++) {
				IField field = board.getField(j, i);
				if (excludeField != field)
					setBombAt((Field) field);
			}
	}

	private boolean setBombAt(Field field) {
		if (field.isBomb())
			return false;
		field.setBomb(true);
		return true;
	}

}
