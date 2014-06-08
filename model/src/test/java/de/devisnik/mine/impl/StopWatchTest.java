package de.devisnik.mine.impl;

import junit.framework.TestCase;

public class StopWatchTest extends TestCase {

	private StopWatch itsStopWatch;

	protected void setUp() throws Exception {
		itsStopWatch = new StopWatch();
	}

	protected void tearDown() throws Exception {
		itsStopWatch = null;
	}

	public void testGetTime() {
		assertEquals(0, itsStopWatch.getTime());
		int startTime = 33;
		assertEquals(startTime, new StopWatch(startTime).getTime());
	}

	public void testTick() {
		int time = itsStopWatch.getTime();
		itsStopWatch.tick();
		assertEquals(time + 1, itsStopWatch.getTime());
	}

	public void testListenerNotification() {
		TestStopWatchListener listener = new TestStopWatchListener();
		itsStopWatch.addListener(listener);
		itsStopWatch.tick();
		assertEquals(1, listener.itsCounter);

		itsStopWatch.removeListener(listener);
		itsStopWatch.tick();
		assertEquals(1, listener.itsCounter);
	}
}
