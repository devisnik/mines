package de.devisnik.mine.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.devisnik.mine.IField;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FieldTest {

    private static final int TOUCHED_STATE = 16;
    private static final int EXPLODED_STATE = 8;
    private static final int FLAGGED_STATE = 4;
    private static final int OPEN_STATE = 2;
    private static final int BOMB_STATE = 1;
    private Field itsField;

    @Before
    public void setUp() {
        itsField = new Field();
    }

    @After
    public void tearDown() {
        itsField = null;
    }

    @Test
    public void testIsBomb() {
        itsField.setBomb(true);
        assertEquals(true, itsField.isBomb());
    }

    @Test
    public void testIsFlagged() {
        itsField.setFlagged(true);
        assertEquals(true, itsField.isFlagged());
    }

    @Test
    public void testIsOpen() {
        itsField.setOpen(true);
        assertEquals(true, itsField.isOpen());
    }

    @Test
    public void testIsExploded() {
        itsField.setExploded(true);
        assertEquals(true, itsField.isExploded());
    }

    @Test
    public void testIsTouched() {
        itsField.setTouched(true);
        assertEquals(true, itsField.isTouched());
    }

    @Test
    public void testGetNeighborBombs() {
        itsField.setNeighborBombs(3);
        assertEquals(3, itsField.getNeighborBombs());
    }

    private Field[] createFields(int number) {
        Field[] fields = new Field[number];
        for (int index = 0; index < fields.length; index++) {
            fields[index] = new Field();
        }
        return fields;
    }

    @Test
    public void testGetNeighborFlags() {
        Field[] neighbors = createFields(3);
        itsField.setNeighbors(neighbors);
        for (int index = 0; index < neighbors.length; index++) {
            neighbors[index].setFlagged(true);
        }
        assertEquals(neighbors.length, itsField.getNeighborFlags());
    }

    @Test
    public void testOpenNotification() {
        TrackingFieldListener listener = new TrackingFieldListener();
        itsField.addListener(listener);
        itsField.setOpen(true);
        assertEquals(1, listener.getOpenChange());
        assertEquals(0, listener.getExplodedChange());
        assertEquals(0, listener.getFlagChange());
        assertEquals(0, listener.getTouchedChange());
    }

    @Test
    public void testExplodedNotification() {
        TrackingFieldListener listener = new TrackingFieldListener();
        itsField.addListener(listener);
        itsField.setExploded(true);
        assertEquals(0, listener.getOpenChange());
        assertEquals(1, listener.getExplodedChange());
        assertEquals(0, listener.getFlagChange());
        assertEquals(0, listener.getTouchedChange());
    }

    @Test
    public void testFlagNotification() {
        TrackingFieldListener listener = new TrackingFieldListener();
        itsField.addListener(listener);
        itsField.setFlagged(true);
        assertEquals(0, listener.getOpenChange());
        assertEquals(0, listener.getExplodedChange());
        assertEquals(1, listener.getFlagChange());
        assertEquals(0, listener.getTouchedChange());
    }

    @Test
    public void testTouchNotification() {
        TrackingFieldListener listener = new TrackingFieldListener();
        itsField.addListener(listener);
        itsField.setTouched(true);
        assertEquals(0, listener.getOpenChange());
        assertEquals(0, listener.getExplodedChange());
        assertEquals(0, listener.getFlagChange());
        assertEquals(1, listener.getTouchedChange());
    }

    @Test
    public void testDefaultState() {
        assertEquals(0, itsField.getState());
    }

    @Test
    public void testBombState() {
        itsField.setBomb(true);
        assertEquals(BOMB_STATE, itsField.getState());
    }

    @Test
    public void testOpenState() {
        itsField.setOpen(true);
        assertEquals(OPEN_STATE, itsField.getState());
    }

    @Test
    public void testFlaggedState() {
        itsField.setFlagged(true);
        assertEquals(FLAGGED_STATE, itsField.getState());
    }

    @Test
    public void testTouchedState() {
        itsField.setTouched(true);
        assertEquals(TOUCHED_STATE, itsField.getState());
    }

    @Test
    public void testExplodedState() {
        itsField.setExploded(true);
        assertEquals(EXPLODED_STATE, itsField.getState());
    }

    @Test
    public void testFlaggedAndExplodedState() {
        itsField.setFlagged(true);
        itsField.setExploded(true);
        assertEquals(FLAGGED_STATE | EXPLODED_STATE, itsField.getState());
    }

    @Test
    public void testBombAndExplodedState() {
        itsField.setBomb(true);
        itsField.setExploded(true);
        assertEquals(BOMB_STATE | EXPLODED_STATE, itsField.getState());
    }

    @Test
    public void testBombAndFlaggedState() {
        itsField.setBomb(true);
        itsField.setFlagged(true);
        assertEquals(BOMB_STATE | FLAGGED_STATE, itsField.getState());
    }

    @Test
    public void testBombAndFlaggedAndExplodedState() {
        itsField.setBomb(true);
        itsField.setFlagged(true);
        itsField.setExploded(true);
        assertEquals(BOMB_STATE | FLAGGED_STATE | EXPLODED_STATE, itsField
                .getState());
    }

    @Test
    public void testCreateFromState() {
        Field field = Field.createFromState(BOMB_STATE | FLAGGED_STATE);
        assertTrue(field.isBomb());
        assertTrue(field.isFlagged());
    }

    @Test
    public void testGetImageIfFlagged() {
        Field field = Field.createFromState(FLAGGED_STATE);
        assertEquals(IField.Image.FLAG, field.getImage());
    }

    @Test
    public void testGetImageIfFlaggedWrong() {
        Field field = Field.createFromState(FLAGGED_STATE | OPEN_STATE);
        assertEquals(IField.Image.FLAG_WRONG, field.getImage());
    }

    @Test
    public void testGetImageIfExploded() {
        Field field = Field.createFromState(BOMB_STATE | OPEN_STATE
                | EXPLODED_STATE);
        assertEquals(IField.Image.BOMB_EXPLODED, field.getImage());
    }

    @Test
    public void testGetImageIfBomb() {
        Field field = Field.createFromState(BOMB_STATE | OPEN_STATE);
        assertEquals(IField.Image.BOMB, field.getImage());
    }

    @Test
    public void testGetImageIfClosed() {
        Field field = Field.createFromState(0);
        assertEquals(IField.Image.CLOSED, field.getImage());
    }

    @Test
    public void testGetImageIfNumber() {
        Field field = Field.createFromState(OPEN_STATE);
        int[] images = new int[]{IField.Image.EMPTY, IField.Image.NO_1,
                IField.Image.NO_2, IField.Image.NO_3, IField.Image.NO_4,
                IField.Image.NO_5, IField.Image.NO_6, IField.Image.NO_7,
                IField.Image.NO_8};
        for (int i = 0; i < images.length; i++) {
            field.setNeighborBombs(i);
            assertEquals(images[i], field.getImage());
        }
    }
}
