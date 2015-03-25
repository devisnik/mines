package de.devisnik.web.client.robot;

import de.devisnik.web.client.robot.impl.FirstConfiguration;

public final class ConfigurationFactory {

	private ConfigurationFactory() {
	}

	public static IConfiguration create(final int setSize, final int subSetSize) {
		return new FirstConfiguration(setSize, subSetSize);
	}

}
