package de.devisnik.mine.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.Point;

public class ExcludeNeighborsBoardMiner implements IBoardMiner {

	@Override
	public void mine(IBoard board, int bombs, IField excludeField) {
		Point dimension = board.getDimension();
		List<Field> excludeList = createExcludeList((Field) excludeField);
		if (bombs >= dimension.x * dimension.y - excludeList.size())
			setAllOthersBombs(board, excludeList, dimension);
		else
			setBombsRandomly(board, bombs, excludeList, dimension);
	}

	private List<Field> createExcludeList(Field field) {
		Field[] neighbors = field.getNeighbors();
		ArrayList<Field> excludeList = new ArrayList<Field>();
		excludeList.add(field);
		for (int i = 0; i < neighbors.length; i++)
			excludeList.add(neighbors[i]);
		return excludeList;
	}

	private void setBombsRandomly(IBoard board, int bombs, List<Field> excludeFields,
			Point dimension) {
		int count = bombs;
		while (count > 0) {
			Field field = getRandomField(board, dimension);
			if (!excludeFields.contains(field))
				if (setBombAt(field))
					count--;
		}
	}

	private Field getRandomField(IBoard board, Point dimension) {
		final int pos = new Random().nextInt(dimension.x * dimension.y);
		final int px = pos % dimension.x;
		final int py = pos / dimension.x;
		return (Field) board.getField(px, py);
	}

	private void setAllOthersBombs(IBoard board, List<Field> excludeFields,
			Point dimension) {
		for (int i = 0; i < dimension.y; i++)
			for (int j = 0; j < dimension.x; j++) {
				Field field = (Field) board.getField(j, i);
				if (!excludeFields.contains(field))
					setBombAt(field);
			}
	}

	private boolean setBombAt(Field field) {
		if (field.isBomb())
			return false;
		field.setBomb(true);
		return true;
	}
}
