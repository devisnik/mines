package de.devisnik.mine.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;

import static org.junit.Assert.assertEquals;

public class StatePersistenceTest {

    @Test
    public void testIntToByteArrayConversion() {
        int NUMBER = 22222;
        byte[] byteArray = StatePersistence.toByteArray(NUMBER);
        int converted = StatePersistence.fromByteArray(byteArray);
        assertEquals(NUMBER, converted);
    }

    @Test
    public void testGameStatePersistence() throws IOException {
        InitialGameState state = new InitialGameState(2, 3, 4);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        StatePersistence.writeGameState(state, stream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        GameState newState = StatePersistence.readGameState(inputStream);
        assertEquals(true, newState.equals(state));
    }
}
