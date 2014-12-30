package de.devisnik.android.mine;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import de.devisnik.mine.IGame;
import de.devisnik.mine.MinesGameAdapter;

public class GameTimer {

	private Handler itsHandler;
	private Timer itsTimer;
	private boolean itsIsPaused = true;
	protected boolean itsIsGameRunning;
	private final IGame itsGame;
	private MinesGameAdapter itsGameListener;

	public GameTimer(IGame game) {
		this.itsGame = game;
		itsIsGameRunning = game.isRunning();
		itsGameListener = new MinesGameAdapter() {
			@Override
			public void onStart() {
				itsIsGameRunning = true;
				startTimer();
			}

			@Override
			public void onBusted() {
				itsIsGameRunning = false;
				stopTimer();
			}

			@Override
			public void onDisarmed() {
				itsIsGameRunning = false;
				stopTimer();
			}
		};
		game.addListener(itsGameListener);
	}

	public void pause() {
		if (!itsIsGameRunning || itsIsPaused)
			return;
		itsIsPaused = true;
		stopTimer();
	}

	public void resume() {
		if (!itsIsGameRunning || !itsIsPaused)
			return;
		itsIsPaused = false;
		startTimer();
	}

	private void stopTimer() {
		if (itsTimer == null)
			return;
		itsTimer.cancel();
		itsTimer = null;
	}

	private void startTimer() {
		if (itsTimer != null)
			return;
		itsTimer = new Timer();
		itsHandler = new Handler();
		itsTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				itsHandler.post(new Runnable() {

					@Override
					public void run() {
						itsGame.tickWatch();
					}
				});
			}
		}, 1000, 1000);
	}

	public void dispose() {
		stopTimer();
		itsGame.removeListener(itsGameListener);
	}
	
}
