package de.devisnik.mine.impl;

import java.util.ArrayList;
import java.util.List;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.Point;

public class Board implements IBoard {

	private Field[] itsFields;
	private final int dimX;
	private final int dimY;
	private boolean isExploded;
	private Field itsTouchField;

	public Board(final int dimX, final int dimY) {
		this(new InitialBoardState(dimX, dimY));
	}

	private Board(final BoardState state) {
		this.dimX = state.getDimX();
		this.dimY = state.getDimY();
		createFieldArray(state);
	}

	public IField getField(final int x, final int y) {
		return itsFields[x * dimY + y];
	}

	private void createFieldArray(final BoardState state) {
		itsFields = new Field[dimX * dimY];
		for (int x = 0; x < dimX; x++) {
			for (int y = 0; y < dimY; y++) {
				itsFields[x * dimY + y] = Field.createFromState(state
						.getFieldState(x, y));
				Field field = itsFields[x * dimY + y];
				if (field.isExploded())
					isExploded = true;
				if (field.isTouched())
					itsTouchField = field;
			}
		}
		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++) {
				itsFields[i * dimY + j].setNeighbors(computeNeighbors(i, j));
			}
		}
		setNeighborBombNumbers();
	}

	private Field[] computeNeighbors(final int x, final int y) {
		final List<Field> neighborList = new ArrayList<Field>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= +1; j++) {
				final int posX = x + i;
				final int posY = y + j;
				if (posX < 0 || posY < 0 || posX >= dimX || posY >= dimY)
					continue;
				if (posX != x || posY != y)
					neighborList.add(itsFields[posX * dimY + posY]);
			}
		}
		return (Field[]) neighborList.toArray(new Field[neighborList.size()]);
	}

	public Point getDimension() {
		return new Point(dimX, dimY);
	}

	public int getFlagCount() {
		int counter = 0;
		for (int i = 0; i < dimY; i++) {
			for (int j = 0; j < dimX; j++) {
				if (itsFields[j * dimY + i].isFlagged())
					counter++;
			}
		}
		return counter;
	}

	public int getOpenCount() {
		int counter = 0;
		for (int i = 0; i < dimY; i++) {
			for (int j = 0; j < dimX; j++) {
				if (itsFields[j * dimY + i].isOpen())
					counter++;
			}
		}
		return counter;
	}

	public void openAllFlaggedOrBomb() {
		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++) {
				Field field = itsFields[i * dimY + j];
				if (field.isBomb() || field.isFlagged())
					field.setOpen(true);
			}
		}
	}

	public String toString() {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < dimY; i++) {
			for (int j = 0; j < dimX; j++) {
				IField field = getField(j, i);
				if (field.isBomb())
					sb.append("B");
				else
					sb.append(field.getNeighborBombs());
				sb.append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public Point getPosition(final IField field) {
		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++) {
				IField current = itsFields[i * dimY + j];
				if (current == field)
					return new Point(i, j);
			}
		}
		throw new RuntimeException("unknown field");
	}

	public boolean isOverflagged() {
		for (int posX = 0; posX < dimX; posX++) {
			for (int posY = 0; posY < dimY; posY++) {
				final Field field = itsFields[posX * dimY + posY];
				if (field.isOpen() && isOverflagged(field))
					return true;
			}
		}
		return false;
	}

	private boolean isOverflagged(final Field field) {
		return field.getNeighborFlags() - field.getNeighborBombs() > 0;
	}

	public void setNeighborBombNumbers() {
		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++) {
				Field field = itsFields[i * dimY + j];
				field.setNeighborBombs(countNeighborBombs(field));
			}
		}
	}

	private int countNeighborBombs(final Field field) {
		int count = 0;
		IField[] neighbors = field.getNeighbors();
		for (int i = 0; i < neighbors.length; i++) {
			IField neighbor = neighbors[i];
			if (neighbor.isBomb())
				count++;
		}
		return count;
	}

	public int getNeighborFlags(final IField field) {
		return ((Field) field).getNeighborFlags();
	}

	private boolean openField(final Field field) {
		if (field.isOpen())
			return false;
		field.setOpen(true);
		// field.setFlagged(false); // evtl flag loeschen
		if (field.isBomb()) {
			field.setExploded(true);
			isExploded = true;
		}
		return true;
	}

	void updateLastTouched(final IField field) {
		if (itsTouchField != null)
			itsTouchField.setTouched(false);
		itsTouchField = (Field) field;
		if (itsTouchField != null)
			itsTouchField.setTouched(true);
	}

	void toggleFlagged(final IField field) {
		((Field) field).setFlagged(!field.isFlagged());
	}

	void openWithNeighbors(final Field field) {
		ArrayList<Field> fieldsToOpen = new ArrayList<Field>();
		fieldsToOpen.add(field);
		// addNonOpenNeighborsToList(field, fieldsToOpen);
		int index = 0;
		while (index < fieldsToOpen.size()) {
			Field indexField = (Field) fieldsToOpen.get(index);
			if (openField(indexField)) {
				if (indexField.getNeighborBombs() == 0)
					addNonOpenNeighborsToList(indexField, fieldsToOpen);
			}
			index++;
		}
	}

	private void addNonOpenNeighborsToList(final Field field,
			final ArrayList<Field> neighborList) {
		IField[] neighbors = field.getNeighbors();
		for (int index = 0; index < neighbors.length; index++) {
			Field indexField = (Field) neighbors[index];
			if (!indexField.isOpen())
				neighborList.add(indexField);
		}
	}

	public boolean isExploded() {
		return isExploded;
	}

	BoardState getState() {
		return BoardState.createFromBoard(this);
	}

	static Board createFromState(final BoardState state) {
		return new Board(state);
	}
}
