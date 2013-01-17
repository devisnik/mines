package de.devisnik.mine.impl;

import java.util.ArrayList;
import java.util.Arrays;
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

	@Override
	public IField getField(final int x, final int y) {
		return itsFields[x * dimY + y];
	}

	private void createFieldArray(final BoardState state) {
		itsFields = new Field[dimX * dimY];
		for (int x = 0; x < dimX; x++)
			for (int y = 0; y < dimY; y++) {
				int index = x * dimY + y;
				Field field = Field.createFromState(state.getFieldState(x, y));
				itsFields[index] = field;
				field.setTag(index);
				if (field.isExploded())
					isExploded = true;
				if (field.isTouched())
					itsTouchField = field;
			}
		for (int i = 0; i < dimX; i++)
			for (int j = 0; j < dimY; j++)
				itsFields[i * dimY + j].setNeighbors(computeNeighbors(i, j));
		setNeighborBombNumbers();
	}

	private Field[] computeNeighbors(final int x, final int y) {
		final List<Field> neighborList = new ArrayList<Field>();
		for (int i = -1; i <= 1; i++)
			for (int j = -1; j <= +1; j++) {
				final int posX = x + i;
				final int posY = y + j;
				if (posX < 0 || posY < 0 || posX >= dimX || posY >= dimY)
					continue;
				if (posX != x || posY != y)
					neighborList.add(itsFields[posX * dimY + posY]);
			}
		return neighborList.toArray(new Field[neighborList.size()]);
	}

	@Override
	public Point getDimension() {
		return new Point(dimX, dimY);
	}

	@Override
	public int getFlagCount() {
		int counter = 0;
		for (Field field : itsFields)
			if (field.isFlagged())
					counter++;
		return counter;
	}

	@Override
	public int getOpenCount() {
		int counter = 0;
		for (Field field : itsFields)
			if (field.isOpen())
				counter++;
		return counter;
	}

	public void openAllFlaggedOrBomb() {
		for (Field field : itsFields)
			if (field.isBomb() || field.isFlagged())
				field.setOpen(true);
	}

	@Override
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

	@Override
	public Point getPosition(final IField field) {
		int tag = ((Field) field).getTag();
		return new Point(tag / dimY, tag % dimY);
	}

	@Override
	public boolean isOverflagged() {
		for (Field field : itsFields)
			if (field.isOpen() && isOverflagged(field))
				return true;
		return false;
	}

	private boolean isOverflagged(final Field field) {
		return field.getNeighborFlags() - field.getNeighborBombs() > 0;
	}

	void setNeighborBombNumbers() {
		for (Field field : itsFields)
			field.setNeighborBombs(countNeighborBombs(field));
	}

	private int countNeighborBombs(final Field field) {
		int count = 0;
		for (Field neighbor : field.getNeighbors())
			if (neighbor.isBomb())
				count++;
		return count;
	}

	int getNeighborFlags(final Field field) {
		return field.getNeighborFlags();
	}

	private boolean openField(final Field field) {
		if (field.isOpen())
			return false;
		field.setOpen(true);
		if (field.isBomb()) {
			field.setExploded(true);
			isExploded = true;
		}
		return true;
	}

	void updateLastTouched(final Field field) {
		if (itsTouchField == field)
			return;
		if (itsTouchField != null)
			itsTouchField.setTouched(false);
		itsTouchField = field;
		if (itsTouchField != null)
			itsTouchField.setTouched(true);
	}

	void toggleFlagged(final Field field) {
		field.setFlagged(!field.isFlagged());
	}

	void openWithNeighbors(final Field field) {
		ArrayList<Field> fieldsToOpen = new ArrayList<Field>();
		fieldsToOpen.add(field);
		int index = 0;
		while (index < fieldsToOpen.size()) {
			Field indexField = fieldsToOpen.get(index);
			if (openField(indexField))
				if (indexField.getNeighborBombs() == 0)
					addNonOpenNeighborsToList(indexField, fieldsToOpen);
			index++;
		}
	}

	private void addNonOpenNeighborsToList(final Field field, final ArrayList<Field> neighborList) {
		for (Field neighbor : field.getNeighbors())
			if (!neighbor.isOpen())
				neighborList.add(neighbor);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dimX;
		result = prime * result + dimY;
		result = prime * result + (isExploded ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(itsFields);
		result = prime * result + (itsTouchField == null ? 0 : itsTouchField.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (dimX != other.dimX)
			return false;
		if (dimY != other.dimY)
			return false;
		if (isExploded != other.isExploded)
			return false;
		if (!Arrays.equals(itsFields, other.itsFields))
			return false;
		if (itsTouchField == null) {
			if (other.itsTouchField != null)
				return false;
		} else if (!itsTouchField.equals(other.itsTouchField))
			return false;
		return true;
	}
}
