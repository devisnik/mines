package de.devisnik.mine;

/**
 * @since 1.0
 */
public interface IField {

	interface Image {
		int EMPTY = 0;
		int NO_1 = 1;
		int NO_2 = 2;
		int NO_3 = 3;
		int NO_4 = 4;
		int NO_5 = 5;
		int NO_6 = 6;
		int NO_7 = 7;
		int NO_8 = 8;
		int BOMB = 9;
		int CLOSED = 10;
		int FLAG = 11;
		int FLAG_WRONG = 12;
		int BOMB_EXPLODED = 13;
	}

	void addListener(IFieldListener listener);

	void removeListener(final IFieldListener listener);

	boolean isBomb();

	boolean isExploded();

	boolean isFlagged();

	boolean isOpen();

	boolean isTouched();

	int getNeighborBombs();
	
	int getImage();
}