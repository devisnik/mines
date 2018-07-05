package de.devisnik.mine.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.devisnik.mine.GameFactory;

import static org.junit.Assert.assertEquals;

public class GameStateTest {

    private GameState itsState;

    @Before
    public void setUp() {
        itsState = new GameState((Game) GameFactory.create(2, 3, 1));
    }

    @Test
    public void testGetBombsCount() {
        assertEquals(1, itsState.getBombsCount());
    }

    @Test
    public void testIsMined() {
        assertEquals(false, itsState.isMined());
    }

    @Test
    public void testWriteRead() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        StatePersistence.writeGameState(itsState, stream);
        byte[] byteArray = stream.toByteArray();
        stream.close();
        GameState restoredState = (GameState) StatePersistence.readGameState(new ByteArrayInputStream(byteArray));
        assertEquals(itsState, restoredState);
    }
}
