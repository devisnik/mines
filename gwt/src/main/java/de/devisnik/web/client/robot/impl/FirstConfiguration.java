package de.devisnik.web.client.robot.impl;

import de.devisnik.web.client.robot.IConfiguration;

public class FirstConfiguration extends Configuration {

	public FirstConfiguration(final int setSize, final int subSetSize) {
		super(createArray(setSize, subSetSize));
	}

	private static boolean[] createArray(final int setSize, final int subSetSize) {
		final boolean[] array = new boolean[setSize];
		for (int i = 0; i < subSetSize; i++) {
			array[i] = true;
		}
		return array;
	}

	public static void main(final String[] args) {
		IConfiguration configuration = new FirstConfiguration(5, 4);
		while (configuration != null) {
//			System.out.println(configuration);
			configuration = configuration.next();
		}
	}
}
