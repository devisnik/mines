package de.devisnik.mine.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StopWatchTest {

	private StopWatch itsStopWatch;

	@Before
	public void setUp() {
		itsStopWatch = new StopWatch();
	}

	@After
	public void tearDown() {
		itsStopWatch = null;
	}

	@Test
	public void testGetTime() {
		assertEquals(0, itsStopWatch.getTime());
		int startTime = 33;
		assertEquals(startTime, new StopWatch(startTime).getTime());
	}

	@Test
	public void testTick() {
		int time = itsStopWatch.getTime();
		itsStopWatch.tick();
		assertEquals(time + 1, itsStopWatch.getTime());
	}

	@Test
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
