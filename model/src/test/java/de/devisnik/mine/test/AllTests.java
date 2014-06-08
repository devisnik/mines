package de.devisnik.mine.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import de.devisnik.mine.impl.BoardStateTest;
import de.devisnik.mine.impl.BoardTest;
import de.devisnik.mine.impl.StatePersistenceTest;
import de.devisnik.mine.impl.ExcludeNeighborsBoardMinerTest;
import de.devisnik.mine.impl.FieldTest;
import de.devisnik.mine.impl.GameStateTest;
import de.devisnik.mine.impl.GameTest;
import de.devisnik.mine.impl.SimpleBoardMinerTest;
import de.devisnik.mine.impl.StopWatchTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for de.devisnik.mine");
		//$JUnit-BEGIN$
		suite.addTestSuite(GameTest.class);
		suite.addTestSuite(BoardTest.class);
		suite.addTestSuite(FieldTest.class);
		suite.addTestSuite(SimpleBoardMinerTest.class);
		suite.addTestSuite(ExcludeNeighborsBoardMinerTest.class);
		suite.addTestSuite(BoardStateTest.class);
		suite.addTestSuite(GameStateTest.class);
		suite.addTestSuite(StatePersistenceTest.class);
		suite.addTestSuite(StopWatchTest.class);
		//$JUnit-END$
		return suite;
	}

}
