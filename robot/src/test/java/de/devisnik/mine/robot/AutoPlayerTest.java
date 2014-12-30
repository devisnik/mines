package de.devisnik.mine.robot;

import de.devisnik.mine.IGame;
import de.devisnik.mine.MinesGameAdapter;
import junit.framework.TestCase;

public class AutoPlayerTest extends TestCase {

	public void test3by3_1() {
		IGame game = TestGameFactory.create3by3_1();
		assertEquals(true, autoPlay(game));
	}

	public void test3by3_2() {
		IGame game = TestGameFactory.create3by3_2();
		assertEquals(false, autoPlay(game));
	}

	public void test3by3_3() {
		IGame game = TestGameFactory.create3by3_3();
		assertEquals(true, autoPlay(game));
	}

	public void test3by3_4() {
		IGame game = TestGameFactory.create3by3_4();
		assertEquals(false, autoPlay(game));
	}

	public void test3by3_5() {
		IGame game = TestGameFactory.create3by3_5();
		assertEquals(true, autoPlay(game));
	}

	public void test3by2_1() {
		IGame game = TestGameFactory.create3by2_1();
		assertEquals(false, autoPlay(game));
	}

	/**
	 * Plays the given game using auto player.
	 * 
	 * @param game
	 * @return <code>true</code> iff game was won, else <code>false</code>
	 */
	private boolean autoPlay(final IGame game) {
		AutoPlayer autoPlayer = new AutoPlayer(game, true);
		final boolean[] done = new boolean[1];
		final boolean[] busted = new boolean[1];
		game.addListener(new MinesGameAdapter() {

			public void onBusted() {
				busted[0] = true;
			}

			public void onDisarmed() {
				done[0] = true;
			}

		});

		game.onRequestOpen(game.getBoard().getField(0, 0));
		while (!done[0] && !busted[0]) {
			autoPlayer.doNextMove();
		}
		return !busted[0];
	}
}
