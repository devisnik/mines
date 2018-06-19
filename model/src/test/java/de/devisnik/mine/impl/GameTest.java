package de.devisnik.mine.impl;

import de.devisnik.mine.IMinesGameListener;
import junit.framework.TestCase;
import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.Point;

public class GameTest extends TestCase {

	private Game itsGame2by1;
	private Game itsGame2by2;
	private IField its2by2UpLeft;
	private TestBoardMiner itsGame2by2miner;

	@Override
	protected void setUp() {
		itsGame2by1 = new Game(2, 1, 1, new SimpleBoardMiner());
		itsGame2by2miner = new TestBoardMiner(new boolean[][] {
				{ false, false }, { true, false } });
		itsGame2by2 = new Game(2, 2, 1, itsGame2by2miner);
		its2by2UpLeft = itsGame2by2.getBoard().getField(0, 0);
	}

	public void testGameSetup() {
		assertEquals(1, itsGame2by1.getBombCount());
		assertEquals(itsGame2by1.getBoard().getDimension(), new Point(2, 1));
	}

	public void testIsStarted() {
		assertEquals(false, itsGame2by1.isStarted());
		openAt(0, 0, itsGame2by1);
		assertEquals(true, itsGame2by1.isStarted());
	}

	public void testFlaggingStartsGame() {
		assertEquals(false, itsGame2by1.isStarted());
		flagAt(0, 0, itsGame2by1);
		assertEquals(true, itsGame2by1.isStarted());
	}

	public void testOpenClosedField() {
		IField left = getFieldAt(0, 0, itsGame2by1);
		assertEquals(false, left.isOpen());
		itsGame2by1.onRequestOpen(left);
		assertEquals(true, left.isOpen());
	}

	public void testOpenOpenField() {
		IField field = openAt(0, 0, itsGame2by1);
		itsGame2by1.onRequestOpen(field);
		assertEquals(true, field.isOpen());
	}

	/**
	 * Open on a flagged field should result in flag removal.
	 */
	public void testOpenFlaggedField() {
		Game game = new Game(2, 2, 2, new TestBoardMiner(new boolean[][]{{false, false}, {true, true}}));
		openAt(0, 0, game);
		IField field = flagAt(1, 1, game);
		game.onRequestOpen(field);
		assertEquals(false, field.isOpen());
		assertEquals(false, field.isFlagged());
	}

	/**
	 * Flagging a closed field should set the FLAG property.
	 */
	public void testFlagClosedField() {
		Game game = new Game(2, 2, 2, new TestBoardMiner(new boolean[][]{{false, false}, {true, true}}));
		openAt(0, 0, game);
		IField field = flagAt(1, 1, game);
		assertEquals(true, field.isFlagged());
	}

	/**
	 * Flagging an already flagged field should remove the flag.
	 */
	public void testFlagFlaggedField() {
        Game game = new Game(2, 2, 2, new TestBoardMiner(new boolean[][]{{false, false}, {true, true}}));
        openAt(0, 0, game);
		IField field = flagAt(1, 1, game);
		game.onRequestFlag(field);
		assertEquals(false, field.isFlagged());
	}

	/**
	 * Flagging an open field should not set the FLAG property.
	 */
	public void testFlagOpenField() {
		IField field = openAt(0, 0, itsGame2by1);
		itsGame2by1.onRequestFlag(field);
		assertEquals(false, field.isFlagged());
	}

	public void testExplodeNotification() {
		openAt(0, 0, itsGame2by1);
		TrackingMinesListener listener = new TrackingMinesListener() {
			@Override
			public void onBusted() {
				// ensure change is called before busted
				assertEquals(1, getChange());
				super.onBusted();
			}
		};
		itsGame2by1.addListener(listener);
		openAt(1, 0, itsGame2by1);
		assertEquals(1, listener.getBusted());
		assertEquals(1, listener.getChange());
		assertEquals(0, listener.getStart());
		assertEquals(0, listener.getDisarmed());
	}

	/**
	 * Tests a bug where neighbors of a bomb are revealed when bomb explodes on
	 * OPEN action.
	 */
    public void testOpenBombDoesNotOpenNeighbors() {
		openAt(1, 0, itsGame2by2);
		assertEquals(true, itsGame2by2.getBoard().getField(1, 0).isOpen());
		assertEquals(true, itsGame2by2.getBoard().getField(1, 0).isExploded());
		assertEquals(false, itsGame2by2.getBoard().getField(0, 0).isOpen());
		assertEquals(false, itsGame2by2.getBoard().getField(0, 1).isOpen());
		assertEquals(false, itsGame2by2.getBoard().getField(1, 1).isOpen());
	}

	public void testFlaggedNotification() {
		Game game = new Game(2, 2, 2, new TestBoardMiner(new boolean[][]{{false, false}, {true, true}}));
		openAt(0, 0, game);
		TrackingMinesListener listener = new TrackingMinesListener() {
			@Override
			public void onChange(final int flags, final int mines) {
				assertEquals(1, flags);
				assertEquals(2, mines);
				super.onChange(flags, mines);
			}
		};
		game.addListener(listener);
		flagAt(1, 1, game);
		assertEquals(0, listener.getStart());
		assertEquals(1, listener.getChange());
		assertEquals(0, listener.getDisarmed());
		assertEquals(0, listener.getBusted());
	}

	public void testDisarmedNotification() {
		openAt(0, 0, itsGame2by1);
		TrackingMinesListener listener = new TrackingMinesListener() {
			@Override
			public void onDisarmed() {
				// Ensure that change is called before disarmed.
				assertEquals(1, getChange());
				super.onDisarmed();
			}
		};
		itsGame2by1.addListener(listener);
		flagAt(1, 0, itsGame2by1);
		assertEquals(1, listener.getChange());
		assertEquals(1, listener.getDisarmed());
		assertEquals(0, listener.getBusted());
		assertEquals(0, listener.getStart());
	}

	public void testStartedNotification() {
		final boolean[] startCall = new boolean[1];
		final boolean[] changeCall = new boolean[1];
		itsGame2by1.addListener(new FailingMinesListener() {
			@Override
			public void onStart() {
				startCall[0] = true;
			}

			@Override
			public void onChange(final int flags, final int mines) {
				assertEquals(true, startCall[0]);
				changeCall[0] = true;
			}
		});
		openAt(0, 0, itsGame2by1);
		assertEquals(true, changeCall[0]);
	}

	/**
	 * the FLAG action on an open field with all available neighboring flags set
	 * should result in opening all non-flagged neighbors.
	 */
	public void testOpenUnflaggedNeighborsOnFlaggingAnOpenField() {
		testOpenUnflaggedNeighbors(new Runnable() {
			@Override
			public void run() {
				itsGame2by2.onRequestFlag(its2by2UpLeft);
			}
		});
	}

	/**
	 * the OPEN action on an already opened field with all available neighboring
	 * flags set should result in opening all non-flagged neighbors.
	 */
	public void testOpenUnflaggedNeighborsOnOpenAnOpenField() {
		testOpenUnflaggedNeighbors(new Runnable() {
			@Override
			public void run() {
				itsGame2by2.onRequestOpen(its2by2UpLeft);
			}
		});
	}

	private void testOpenUnflaggedNeighbors(final Runnable action) {
		itsGame2by2.onRequestOpen(its2by2UpLeft);

		final boolean[] disarmedCall = new boolean[1];
		final int[] changeCallCount = new int[] { 0 };
		itsGame2by2.addListener(new FailingMinesListener() {
			@Override
			public void onChange(final int flags, final int mines) {
				changeCallCount[0]++;
			}

			@Override
			public void onDisarmed() {
				disarmedCall[0] = true;
			}
		});
		flagAt(1, 0, itsGame2by2);
		action.run();
		assertEquals(true, getFieldAt(0, 1, itsGame2by2).isOpen());
		assertEquals(true, getFieldAt(1, 1, itsGame2by2).isOpen());
		assertEquals(true, disarmedCall[0]);
		assertEquals(2, changeCallCount[0]);
	}

	/**
	 * reproduces a bug condition forcing StackOverflow Error due to recursive
	 * neighbor opening.
	 * 
	 */
	public void testOpenManyUnflaggedNeighborsOnOpen() {
		Game game = new Game(100, 100, 1, new IBoardMiner() {

			@Override
			public void mine(final IBoard board, final int bombs, final IField clickedField) {
				((Field) board.getField(0, 0)).setBomb(true);
			}
		});
		openAt(1, 0, game);
		openAt(1, 1, game);
		openAt(0, 1, game);

		game.addListener(new FailingMinesListener() {
			@Override
			public void onChange(final int flags, final int mines) {
				assertEquals(0, flags);
			}
		});
		openAt(2, 0, game);
	}

	/**
	 * Opening an empty field (no bombs in neighborhood) should open the field
	 * and all neighbors. For empty neighbors the whole neighborhood is opened.
	 */
	public void testOpenNeighborsOnOpenEmptyField() {
		Game game = new Game(3, 3, 1, new TestBoardMiner(new boolean[][] {
				{ true, false, false }, { false, false, false },
				{ false, false, false } }));
		openAt(2, 0, game);
		assertEquals(8, game.getBoard().getOpenCount());
	}

	public void testGetAndCreateFromState() {
		GameState initialState = itsGame2by2.getState();
		IField field = openAt(0, 0, itsGame2by2);
		itsGame2by2.tickWatch();
		GameState stateAfterOpen = itsGame2by2.getState();
		assertEquals(true, field.isOpen());
		Game initialGame = Game.createFromState(initialState, itsGame2by2miner);
		assertEquals(false, getFieldAt(0, 0, initialGame).isOpen());
		Game gameAfterOpen = Game.createFromState(stateAfterOpen,
				itsGame2by2miner);
		assertEquals(true, getFieldAt(0, 0, gameAfterOpen).isOpen());
		assertEquals(1, gameAfterOpen.getWatch().getTime());
	}

	public void testNoTickingForUnstartedGame() {
		itsGame2by1.tickWatch();
		assertEquals(0, itsGame2by1.getWatch().getTime());
	}

	public void testTickingForStartedGame() {
		openAt(0, 0, itsGame2by1);
		itsGame2by1.tickWatch();
		assertEquals(1, itsGame2by1.getWatch().getTime());
	}

	public void testNoTickingForSolvedGame() {
		openAt(0, 0, itsGame2by1);
		flagAt(1, 0, itsGame2by1);
		itsGame2by1.tickWatch();
		assertEquals(0, itsGame2by1.getWatch().getTime());
	}

	public void testNoTickingForExplodedGame() {
		openAt(0, 0, itsGame2by1);
		openAt(1, 0, itsGame2by1);
		itsGame2by1.tickWatch();
		assertEquals(0, itsGame2by1.getWatch().getTime());
	}

	/*
	There was a bug where a flagged field in an empty area was marked as invalidly flagged
	when a neighbor was opened within a running game.
	We now just open it, removing the (invalid) flag.
	 */
	public void testOpenRemovesInvalidFlagOnEmptyNeighborFieldAndOpens() {
		TestBoardMiner miner = new TestBoardMiner(new boolean[][]{
				{false, false, false},
				{false, false, false},
				{true, false, false}
		});
		Game game = new Game(3, 3, 1, miner);
		game.onRequestFlag(getFieldAt(0,0,game));
		game.onRequestOpen(getFieldAt(0,1,game));

		assertEquals(false, game.getBoard().getField(0,0).isFlagged());
		assertEquals(true, game.getBoard().getField(0,0).isOpen());
	}

    private IField getFieldAt(final int x, final int y, final Game game) {
		return game.getBoard().getField(x, y);
	}

	private IField openAt(final int x, final int y, final Game game) {
		IField field = getFieldAt(x, y, game);
		game.onRequestOpen(field);
		return field;
	}

	private IField flagAt(final int x, final int y, final Game game) {
		IField field = getFieldAt(x, y, game);
		game.onRequestFlag(field);
		return field;
	}

	public static class FailingMinesListener implements IMinesGameListener {

        public void onBusted() {
            fail();
        }

        public void onChange(int flags, int mines) {
            fail();
        }

        public void onDisarmed() {
            fail();
        }

        public void onStart() {
            fail();
        }

    }
}
