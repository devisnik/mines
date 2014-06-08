package de.devisnik.android.mine;

import android.os.Vibrator;
import android.widget.Toast;
import de.devisnik.mine.IGame;
import de.devisnik.mine.IMinesGameListener;

public class MessagesController implements IMinesGameListener {

	private final IGame itsGame;
	private boolean itsIsStarted;
	private final MineSweeper itsContext;
	private final Settings itsSettings;

	public MessagesController(final IGame game, final MineSweeper context, Settings settings) {
		itsGame = game;
		itsContext = context;
		itsSettings = settings;
		itsIsStarted = itsGame.isStarted();
		itsGame.addListener(this);
	}
 
	public void dispose() {
		itsGame.removeListener(this);
	}

	private void showToast(int messageId, int length) {
		Toast.makeText(itsContext, messageId, length).show();
	}
	
	@Override
	public void onBusted() {
		runExplodeVibration();
		showToast(R.string.toast_lose, Toast.LENGTH_LONG);
	}

	@Override
	public void onChange(final int flags, final int mines) {
		if (!itsIsStarted)
			showToast(R.string.toast_longpress, Toast.LENGTH_SHORT);
	}

	@Override
	public void onDisarmed() {
		showToast(R.string.toast_win, Toast.LENGTH_LONG);
	}

	@Override
	public void onStart() {
		itsIsStarted = true;
	}

	private void runExplodeVibration() {
		final Vibrator vibrator = itsSettings.getVibrator();
		if (vibrator != null)
			vibrator.vibrate(new long[] { 1, 50, 100, 150, 200, 300 }, -1);
	}

}
