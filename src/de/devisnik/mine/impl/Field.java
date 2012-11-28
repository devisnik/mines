package de.devisnik.mine.impl;

import java.util.ArrayList;

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
	private final ArrayList<IFieldListener> itsListeners = new ArrayList<IFieldListener>();
	private Field[] neighbors;
	private boolean isExploded;
	private int bombCount;
	private boolean isTouched;

	Field() {
		this(0);
	}

	private Field(int state) {
		setState(state);
	}

	public void addListener(IFieldListener listener) {
		itsListeners.add(listener);
	}

	private void fireFieldOpened(boolean value) {
		for (int i = 0; i < itsListeners.size(); i++) {
			IFieldListener listener = (IFieldListener) itsListeners.get(i);
			listener.onFieldOpenChange(this, value);
		}
	}

	private void fireFieldExploded(boolean value) {
		for (int i = 0; i < itsListeners.size(); i++) {
			IFieldListener listener = (IFieldListener) itsListeners.get(i);
			listener.onFieldExplodedChange(this, value);
		}
	}

	private void fireFieldFlagged(boolean value) {
		for (int i = 0; i < itsListeners.size(); i++) {
			IFieldListener listener = (IFieldListener) itsListeners.get(i);
			listener.onFieldFlagChange(this, value);
		}
	}

	public IField[] getNeighbors() {
		return neighbors;
	}

	public boolean isBomb() {
		return isBomb;
	}

	public boolean isFlagged() {
		return isFlagged;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public boolean isExploded() {
		return isExploded;
	}

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

	private void fireFieldTouched(boolean value) {
		for (int i = 0; i < itsListeners.size(); i++) {
			IFieldListener listener = (IFieldListener) itsListeners.get(i);
			listener.onFieldTouchedChange(this, value);
		}
	}

	public boolean isTouched() {
		return isTouched;
	}

	int getNeighborFlags() {
		int counter = 0;
		for (int i = 0; i < neighbors.length; i++) {
			final IField neighbor = neighbors[i];
			if (neighbor.isFlagged())
				counter++;
		}
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
		return (value & (1 << position)) != 0;
	}

	private static int setBit(int position, int value, boolean booleanValue) {
		if (booleanValue)
			return value | (1 << position);
		return value & ~(1 << position);
	}

	public static Field createFromState(int state) {
		return new Field(state);
	}

	void setNeighborBombs(int number) {
		bombCount = number;
	}

	public int getNeighborBombs() {
		return bombCount;
	}

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
			} else {
				if (isFlagged())
					imgnr = Image.FLAG_WRONG;
				else
					imgnr = getNeighborBombs();
			}
		} else {
			if (isFlagged())
				imgnr = Image.FLAG;
			else
				imgnr = Image.CLOSED;
		}
		return imgnr;
	}
}
