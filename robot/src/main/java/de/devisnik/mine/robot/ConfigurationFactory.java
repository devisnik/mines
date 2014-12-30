package de.devisnik.mine.robot;

import de.devisnik.mine.robot.impl.FirstConfiguration;

public final class ConfigurationFactory {

	private ConfigurationFactory() {
	}

	public static IConfiguration create(final int setSize, final int subSetSize) {
		return new FirstConfiguration(setSize, subSetSize);
	}

}
