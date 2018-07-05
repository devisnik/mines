package de.devisnik.mine.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardStateTest {

    private BoardState itsBoardState;
    private Board itsBoard;

    @Before
    public void setUp() {
        itsBoard = new Board(2, 3);
        itsBoardState = itsBoard.getState();
    }

    @After
    public void tearDown() {
        itsBoard = null;
        itsBoardState = null;
    }

    @Test
    public void testGetDimensions() {
        assertEquals(2, itsBoardState.getDimX());
        assertEquals(3, itsBoardState.getDimY());
    }

    @Test
    public void testGetFieldState() {
        assertEquals(0, itsBoardState.getFieldState(0, 0));
    }

    @Test
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
