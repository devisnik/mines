package de.devisnik.mine.impl;

import java.util.ArrayList;
import java.util.List;

import de.devisnik.mine.IField;
import de.devisnik.mine.IFieldListener;

public class Field implements IField {

	private static final int STATE_BOMB = 0;
	private static final int STATE_OPEN = 1;
	private static final int STATE_FLAGGED = 2;
	private static final int STATE_EXPLODED = 3;
	private static final int STATE_TOUCHED = 4;

	private boolean isBomb;
	private boolean isFlagged;
	private boolean isOpen;
	private final List<IFieldListener> itsListeners = new ArrayList<IFieldListener>();
	private Field[] neighbors;
	private boolean isExploded;
	private int bombCount;
	private boolean isTouched;

	private int tag;

	Field() {
		this(0);
	}

	private Field(int state) {
		setState(state);
	}

	@Override
	public void addListener(IFieldListener listener) {
		itsListeners.add(listener);
	}

	private void fireFieldOpened(boolean value) {
		for (IFieldListener listener : itsListeners)
			listener.onFieldOpenChange(this, value);
	}

	private void fireFieldExploded(boolean value) {
		for (IFieldListener listener : itsListeners)
			listener.onFieldExplodedChange(this, value);
	}

	private void fireFieldFlagged(boolean value) {
		for (IFieldListener listener : itsListeners)
			listener.onFieldFlagChange(this, value);
	}

	private void fireFieldTouched(boolean value) {
		for (IFieldListener listener : itsListeners)
			listener.onFieldTouchedChange(this, value);
	}

	Field[] getNeighbors() {
		return neighbors;
	}

	@Override
	public boolean isBomb() {
		return isBomb;
	}

	@Override
	public boolean isFlagged() {
		return isFlagged;
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

	@Override
	public boolean isExploded() {
		return isExploded;
	}

	@Override
	public void removeListener(final IFieldListener listener) {
		itsListeners.remove(listener);
	}

	void setBomb(final boolean value) {
		isBomb = value;
	}

	void setTouched(final boolean value) {
		if (isTouched == value)
			return;
		isTouched = value;
		fireFieldTouched(value);
	}

	@Override
	public boolean isTouched() {
		return isTouched;
	}

	int getNeighborFlags() {
		int counter = 0;
		for (IField neighbor : neighbors)
			if (neighbor.isFlagged())
				counter++;
		return counter;
	}

	public void setFlagged(final boolean value) {
		if (isFlagged == value)
			return;
		isFlagged = value;
		fireFieldFlagged(isFlagged);
	}

	void setNeighbors(final Field[] neighbors) {
		this.neighbors = neighbors;
	}

	void setOpen(final boolean value) {
		if (isOpen == value)
			return;
		isOpen = value;
		fireFieldOpened(isOpen);
	}

	void setExploded(final boolean value) {
		if (isExploded == value)
			return;
		isExploded = value;
		fireFieldExploded(isExploded);
	}

	public int getState() {
		int state = 0;
		state = setBit(STATE_EXPLODED, state, isExploded);
		state = setBit(STATE_FLAGGED, state, isFlagged);
		state = setBit(STATE_OPEN, state, isOpen);
		state = setBit(STATE_BOMB, state, isBomb);
		state = setBit(STATE_TOUCHED, state, isTouched);
		return state;
	}

	private void setState(int state) {
		isTouched = getBit(STATE_TOUCHED, state);
		isBomb = getBit(STATE_BOMB, state);
		isOpen = getBit(STATE_OPEN, state);
		isFlagged = getBit(STATE_FLAGGED, state);
		isExploded = getBit(STATE_EXPLODED, state);
	}

	private static boolean getBit(int position, int value) {
		return (value & 1 << position) != 0;
	}

	private static int setBit(int position, int value, boolean booleanValue) {
		if (booleanValue)
			return value | 1 << position;
		return value & ~(1 << position);
	}

	public static Field createFromState(int state) {
		return new Field(state);
	}

	void setNeighborBombs(int number) {
		bombCount = number;
	}

	@Override
	public int getNeighborBombs() {
		return bombCount;
	}

	@Override
	public int getImage() {
		int imgnr = 0;
		if (isOpen()) {
			if (isBomb()) {
				if (isExploded())
					imgnr = Image.BOMB_EXPLODED;
				else if (isFlagged())
					imgnr = Image.FLAG;
				else
					imgnr = Image.BOMB;
			} else if (isFlagged())
				imgnr = Image.FLAG_WRONG;
			else
				imgnr = getNeighborBombs();
		} else if (isFlagged())
			imgnr = Image.FLAG;
		else
			imgnr = Image.CLOSED;
		return imgnr;
	}

	int getTag() {
		return tag;
	}

	void setTag(int tag) {
		this.tag = tag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bombCount;
		result = prime * result + (isBomb ? 1231 : 1237);
		result = prime * result + (isExploded ? 1231 : 1237);
		result = prime * result + (isFlagged ? 1231 : 1237);
		result = prime * result + (isOpen ? 1231 : 1237);
		result = prime * result + (isTouched ? 1231 : 1237);
		result = prime * result + tag;
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
		Field other = (Field) obj;
		if (bombCount != other.bombCount)
			return false;
		if (isBomb != other.isBomb)
			return false;
		if (isExploded != other.isExploded)
			return false;
		if (isFlagged != other.isFlagged)
			return false;
		if (isOpen != other.isOpen)
			return false;
		if (isTouched != other.isTouched)
			return false;
		if (tag != other.tag)
			return false;
		return true;
	}

}
