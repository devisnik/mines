package de.devisnik.mine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

public class GameFactoryTest extends TestCase {

	public void testCreate() {
		IGame game = GameFactory.create(4, 6, 8);
		assertEquals(8, game.getBombCount());
		assertEquals(new Point(4, 6), game.getBoard().getDimension());
	}

	public void testPersistence() throws IOException {
		IGame originalGame = GameFactory.create(4, 6, 8);
		File file = File.createTempFile("gamefactory", "persist");
		FileOutputStream fos = new FileOutputStream(file);
		GameFactory.writeToStream(originalGame, fos);
		fos.close();
		FileInputStream fis = new FileInputStream(file);
		IGame restoredGame = GameFactory.readFromStream(fis);
		assertEquals(originalGame, restoredGame);
	}

}
