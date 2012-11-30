package de.devisnik.mine.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.Point;

public class BoardTest extends TestCase {
	private Board itsBoard;

	@Override
	protected void setUp() throws Exception {
		itsBoard = new Board(2, 2);
	}

	@Override
	protected void tearDown() throws Exception {
		itsBoard = null;
	}

	public void testGetDimension() {
		Point dimension = itsBoard.getDimension();
		assertEquals(2, dimension.x);
		assertEquals(2, dimension.y);
	}

	public void testGetPosition() {
		Point dimension = itsBoard.getDimension();
		for (int i = 0; i < dimension.x; i++)
			for (int j = 0; j < dimension.y; j++) {
				IField field = itsBoard.getField(i, j);
				Point position = itsBoard.getPosition(field);
				assertEquals(i, position.x);
				assertEquals(j, position.y);
			}
	}

	public void testOpenAllFlaggedOrBomb() {
		flag(0, 0);
		bomb(0, 1);
		itsBoard.openAllFlaggedOrBomb();
		Point dimension = itsBoard.getDimension();
		int count = 0;
		for (int i = 0; i < dimension.x; i++)
			for (int j = 0; j < dimension.y; j++) {
				IField field = itsBoard.getField(i, j);
				if (field.isOpen()) {
					count++;
					assertTrue(field.isFlagged() || field.isBomb());
				}
			}
		assertEquals(2, count);
	}

	public void testIsOverflagged() {
		flag(0, 0);
		bomb(0, 0);
		flag(1, 0);
		open(1, 1);
		assertEquals(true, itsBoard.isOverflagged());
	}

	public void testIsOverflaggedBeforeOpen() {
		flag(0, 0);
		flag(1, 0);
		assertEquals(false, itsBoard.isOverflagged());
	}

	public void testGetFlagCount() {
		flag(0, 0);
		assertEquals(1, itsBoard.getFlagCount());
	}

	public void testGetBombCount() {
		open(1, 1);
		open(0, 0);
		assertEquals(2, itsBoard.getOpenCount());
	}

	public void testGetNeighbors() {
		Board board = new Board(3, 3);
		assertNeighbors(board.getField(0, 0), board, new int[][] { { 1, 0 },
				{ 0, 1 }, { 1, 1 } });
		assertNeighbors(board.getField(1, 0), board, new int[][] { { 0, 0 },
				{ 0, 1 }, { 1, 1 }, { 2, 1 }, { 2, 0 } });
		assertNeighbors(board.getField(1, 1), board, new int[][] { { 0, 0 },
				{ 0, 1 }, { 0, 2 }, { 1, 2 }, { 2, 2 }, { 2, 1 }, { 2, 0 },
				{ 1, 0 } });

	}

	private void assertNeighbors(IField field, IBoard board, int[][] positions) {
		List<IField> neighbors = new ArrayList<IField>();
		neighbors.addAll(Arrays.asList(field.getNeighbors()));
		assertEquals(neighbors.size(), positions.length);
		for (int i = 0; i < positions.length; i++) {
			int[] position = positions[i];
			IField neighbor = board.getField(position[0], position[1]);
			assertTrue(neighbors.contains(neighbor));
			neighbors.remove(neighbor);
		}

	}

	private Field flag(int x, int y) {
		Field field = (Field) itsBoard.getField(x, y);
		field.setFlagged(true);
		return field;
	}

	private Field bomb(int x, int y) {
		Field field = (Field) itsBoard.getField(x, y);
		field.setBomb(true);
		return field;
	}

	private Field open(int x, int y) {
		Field field = (Field) itsBoard.getField(x, y);
		field.setOpen(true);
		return field;
	}

	public void testGestState() {
		bomb(1, 1);
		open(0, 0);
		flag(1, 1);
		BoardState state = itsBoard.getState();
		Point dimension = itsBoard.getDimension();
		assertEquals(dimension.x, state.getDimX());
		assertEquals(dimension.y, state.getDimY());
		for (int x = 0; x < dimension.x; x++)
			for (int y = 0; y < dimension.y; y++)
				assertEquals(((Field) itsBoard.getField(x, y)).getState(),
						state.getFieldState(x,y));
	}

	public void testCreateFromState() {
		bomb(1, 1).setFlagged(true);
		open(0, 0);
		BoardState state = itsBoard.getState();
		Board board = Board.createFromState(state);
		assertEquals(itsBoard.getDimension(), board.getDimension());
		assertEquals(true, board.getField(1, 1).isBomb());
		assertEquals(true, board.getField(0, 0).isOpen());
		assertEquals(true, board.getField(1, 1).isFlagged());
		assertEquals(false, board.getField(0, 1).isOpen());
	}

	public void testExploded() {
		assertEquals(false, itsBoard.isExploded());
		itsBoard.openWithNeighbors(bomb(1, 1));
		assertEquals(true, itsBoard.isExploded());
	}

	public void testCreateExplodedFromState() {
		itsBoard.openWithNeighbors(bomb(1, 1));
		Board board = Board.createFromState(itsBoard.getState());
		assertEquals(true, board.isExploded());
	}
}
