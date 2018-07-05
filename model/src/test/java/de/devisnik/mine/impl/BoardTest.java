package de.devisnik.mine.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.IFieldListener;
import de.devisnik.mine.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BoardTest {
    private Board itsBoard;

    @Before
    public void setUp() {
        itsBoard = new Board(2, 2);
    }

    @After
    public void tearDown() {
        itsBoard = null;
    }

    @Test
    public void testGetDimension() {
        Point dimension = itsBoard.getDimension();
        assertEquals(2, dimension.x);
        assertEquals(2, dimension.y);
    }

    @Test
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

    @Test
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

    @Test
    public void testIsOverflagged() {
        flag(0, 0);
        bomb(0, 0);
        flag(1, 0);
        open(1, 1);
        assertEquals(true, itsBoard.isOverflagged());
    }

    @Test
    public void testIsOverflaggedBeforeOpen() {
        flag(0, 0);
        flag(1, 0);
        assertEquals(false, itsBoard.isOverflagged());
    }

    @Test
    public void testGetFlagCount() {
        flag(0, 0);
        assertEquals(1, itsBoard.getFlagCount());
    }

    @Test
    public void testGetBombCount() {
        open(1, 1);
        open(0, 0);
        assertEquals(2, itsBoard.getOpenCount());
    }

    @Test
    public void testGetNeighbors() {
        Board board = new Board(3, 3);
        assertNeighbors((Field) board.getField(0, 0), board, new int[][]{{1, 0},
                {0, 1}, {1, 1}});
        assertNeighbors((Field) board.getField(1, 0), board, new int[][]{{0, 0},
                {0, 1}, {1, 1}, {2, 1}, {2, 0}});
        assertNeighbors((Field) board.getField(1, 1), board, new int[][]{{0, 0},
                {0, 1}, {0, 2}, {1, 2}, {2, 2}, {2, 1}, {2, 0},
                {1, 0}});

    }

    private void assertNeighbors(Field field, IBoard board, int[][] positions) {
        List<Field> neighbors = new ArrayList<Field>();
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

    @Test
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
                        state.getFieldState(x, y));
    }

    @Test
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

    @Test
    public void testExploded() {
        assertEquals(false, itsBoard.isExploded());
        itsBoard.openWithNeighbors(bomb(1, 1));
        assertEquals(true, itsBoard.isExploded());
    }

    @Test
    public void testCreateExplodedFromState() {
        itsBoard.openWithNeighbors(bomb(1, 1));
        Board board = Board.createFromState(itsBoard.getState());
        assertEquals(true, board.isExploded());
    }

    @Test
    public void testUpdateTouchedField() {
        final Field field = (Field) itsBoard.getField(0, 0);
        final int[] counter = new int[1];
        field.addListener(new IFieldListener() {

            @Override
            public void onFieldTouchedChange(IField ifield, boolean value) {
                counter[0]++;
                assertEquals(field, ifield);
            }

            @Override
            public void onFieldOpenChange(IField field, boolean value) {
                fail();
            }

            @Override
            public void onFieldFlagChange(IField field, boolean value) {
                fail();
            }

            @Override
            public void onFieldExplodedChange(IField field, boolean value) {
                fail();
            }
        });
        itsBoard.updateLastTouched(field);
        assertEquals(1, counter[0]);
    }

    @Test
    public void testResetTouchOnAlreadyTouchedField() {
        final Field field = (Field) itsBoard.getField(0, 0);
        itsBoard.updateLastTouched(field);
        final int[] counter = new int[1];
        field.addListener(new IFieldListener() {

            @Override
            public void onFieldTouchedChange(IField ifield, boolean value) {
                counter[0]++;
                assertEquals(field, ifield);
            }

            @Override
            public void onFieldOpenChange(IField field, boolean value) {
                fail();
            }

            @Override
            public void onFieldFlagChange(IField field, boolean value) {
                fail();
            }

            @Override
            public void onFieldExplodedChange(IField field, boolean value) {
                fail();
            }
        });
        itsBoard.updateLastTouched(field);
        assertEquals(0, counter[0]);
    }
}
