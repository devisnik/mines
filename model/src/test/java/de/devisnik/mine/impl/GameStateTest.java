package de.devisnik.mine.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.devisnik.mine.GameFactory;
import junit.framework.TestCase;

public class GameStateTest extends TestCase {

	private GameState itsState;

	protected void setUp() throws Exception {
		itsState = new GameState((Game) GameFactory.create(2, 3, 1));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetBombsCount() {
		assertEquals(1, itsState.getBombsCount());
	}

	public void testIsMined() {
		assertEquals(false, itsState.isMined());		
	}

	public void testWriteRead() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		StatePersistence.writeGameState(itsState, stream);
		byte[] byteArray = stream.toByteArray();
		stream.close();
		GameState restoredState = (GameState) StatePersistence.readGameState(new ByteArrayInputStream(byteArray));
		assertEquals(itsState, restoredState);
	}
}
