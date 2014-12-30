package de.devisnik.mine.robot;

import de.devisnik.mine.robot.IConfiguration.Element;
import junit.framework.TestCase;

public class ConfigurationTest extends TestCase {

	public void testNext() {
		int setSize = 9;
		int subSetSize = 4;

		IConfiguration configuration = ConfigurationFactory.create(setSize,
				subSetSize);

		testLengthOfEachConfiguration(setSize, configuration);
		testNumberOfConfigurations(setSize, subSetSize, configuration);
		testNumberOfSelectedElements(setSize, subSetSize, configuration);
	}

	private void testNumberOfSelectedElements(int setSize, int subSetSize,
			IConfiguration configuration) {
		int[] flagCounter = new int[setSize];
		while (configuration != null) {
			Element[] elements = configuration.getElements();
			int flaggedElementsCount = 0;
			for (int i = 0; i < elements.length; i++) {
				if (elements[i].isFlag()) {
					flagCounter[elements[i].getPosition()]++;
					flaggedElementsCount++;
				}
			}
			assertEquals(subSetSize, flaggedElementsCount);
			configuration = configuration.next();
		}
		for (int i = 0; i < flagCounter.length; i++) {
			assertEquals(choose(setSize, subSetSize)*subSetSize/setSize, flagCounter[i]);
		}
	}

	private void testNumberOfConfigurations(int setSize, int subSetSize,
			IConfiguration configuration) {
		assertEquals(choose(setSize, subSetSize), countConfigurations(setSize,
				configuration));
	}

	private void testLengthOfEachConfiguration(int setSize,
			IConfiguration configuration) {
		IConfiguration currentConfiguration = configuration; 
		while (currentConfiguration != null) {
			assertEquals(setSize, currentConfiguration.getElementsCount());
			currentConfiguration = currentConfiguration.next();
		}
	}

	private int countConfigurations(int setSize,
			IConfiguration startConfiguration) {
		int count = 0;
		while (startConfiguration != null) {
			count++;
			startConfiguration = startConfiguration.next();
		}
		return count;
	}

	private int factorial(int number) {
		if (number < 0) {
			throw new IllegalArgumentException("no valid number: " + number);
		}
		if (number == 0)
			return 1;
		if (number == 1)
			return 1;
		return number * factorial(number - 1);
	}

	private int choose(int setSize, int subSetSize) {
		return factorial(setSize) / factorial(subSetSize)
				/ factorial(setSize - subSetSize);
	}

	public void testGetIntersection() {
		IConfiguration configuration = ConfigurationFactory.create(4, 2);
		IConfiguration next = configuration.next();
		IConfiguration intersection = configuration.getIntersection(next);
		assertNotNull(intersection);
		assertEquals(2, intersection.getElementsCount());
	}

}
