package de.devisnik.mine.robot.test;

import de.devisnik.mine.robot.AutoPlayerTest;
import de.devisnik.mine.robot.ConfigurationTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for de.devisnik.mine.robot");
		//$JUnit-BEGIN$
		suite.addTestSuite(AutoPlayerTest.class);
		suite.addTestSuite(ConfigurationTest.class);
		//$JUnit-END$
		return suite;
	}

}
