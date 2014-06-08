package de.devisnik.mine.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

public class BoardStateTest extends TestCase {

	private BoardState itsBoardState;
	private Board itsBoard;

	@Override
	protected void setUp() throws Exception {
		itsBoard = new Board(2, 3);
		itsBoardState = itsBoard.getState();
	}

	@Override
	protected void tearDown() throws Exception {
		itsBoard = null;
		itsBoardState = null;
	}

	public void testGetDimensions() {
		assertEquals(2, itsBoardState.getDimX());
		assertEquals(3, itsBoardState.getDimY());
	}

	public void testGetFieldState() {
		assertEquals(0, itsBoardState.getFieldState(0, 0));
	}

	public void testWrite() throws IOException {
		itsBoard.toggleFlagged((Field) itsBoard.getField(0, 0));
		BoardState boardState = itsBoard.getState();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		StatePersistence.writeBoardState(boardState, outputStream);
		outputStream.flush();
		byte[] byteArray = outputStream.toByteArray();
		outputStream.close();
		BoardState restoredState = StatePersistence.readBoardState(new ByteArrayInputStream(byteArray));
		assertEquals(boardState, restoredState);
	}

}
