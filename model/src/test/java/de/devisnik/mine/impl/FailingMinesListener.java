package de.devisnik.mine.impl;

import de.devisnik.mine.IMinesGameListener;
import junit.framework.TestCase;

public class FailingMinesListener implements IMinesGameListener {

	public void onBusted() {
		TestCase.fail();
	}

	public void onChange(int flags, int mines) {
		TestCase.fail();
	}

	public void onDisarmed() {
		TestCase.fail();
	}

	public void onStart() {
		TestCase.fail();
	}

}
