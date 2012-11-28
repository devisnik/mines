package de.devisnik.mine.impl;

import java.util.ArrayList;
import java.util.Random;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.Point;

public class ExcludeNeighborsBoardMiner implements IBoardMiner {

	public void mine(IBoard board, int bombs, IField excludeField) {
		Point dimension = board.getDimension();
		ArrayList excludeList = createExcludeList(excludeField);
		if (bombs >= dimension.x * dimension.y - excludeList.size())
			setAllOthersBombs(board, excludeList, dimension);
		else
			setBombsRandomly(board, bombs, excludeList, dimension);
	}

	private ArrayList createExcludeList(IField field) {
		IField[] neighbors = field.getNeighbors();
		ArrayList excludeList = new ArrayList();
		excludeList.add(field);
		for (int i = 0; i < neighbors.length; i++)
			excludeList.add(neighbors[i]);
		return excludeList;
	}

	private void setBombsRandomly(IBoard board, int bombs, ArrayList excludeFields,
			Point dimension) {
		int count = bombs;
		while (count > 0) {
			IField field = getRandomField(board, dimension);
			if (!excludeFields.contains(field)) {
				if (setBombAt((Field) field))
					count--;
			}
		}
	}

	private IField getRandomField(IBoard board, Point dimension) {
		final int pos = new Random().nextInt(dimension.x * dimension.y);
		final int px = pos % dimension.x;
		final int py = pos / dimension.x;
		IField field = board.getField(px, py);
		return field;
	}

	private void setAllOthersBombs(IBoard board, ArrayList excludeFields,
			Point dimension) {
		for (int i = 0; i < dimension.y; i++) {
			for (int j = 0; j < dimension.x; j++) {
				IField field = board.getField(j, i);
				if (!excludeFields.contains(field))
					setBombAt((Field) field);
			}
		}
	}

	private boolean setBombAt(Field field) {
		if (field.isBomb())
			return false;
		field.setBomb(true);
		return true;
	}
}
