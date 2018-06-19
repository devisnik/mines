package de.devisnik.android.mine;

import android.os.Vibrator;
import android.widget.Toast;

import de.devisnik.mine.IGame;
import de.devisnik.mine.IMinesGameListener;

public class MessagesController implements IMinesGameListener {

	private final IGame game;
	private final MineSweeper context;
	private final Settings settings;

	public MessagesController(final IGame game, final MineSweeper context, Settings settings) {
		this.game = game;
		this.context = context;
		this.settings = settings;
		game.addListener(this);
	}
 
	public void dispose() {
		game.removeListener(this);
	}

	private void showToast(int messageId, int length) {
		Toast.makeText(context, messageId, length).show();
	}
	
	@Override
	public void onBusted() {
		runExplodeVibration();
		showToast(R.string.toast_lose, Toast.LENGTH_SHORT);
	}

	@Override
	public void onChange(final int flags, final int mines) {
	}

	@Override
	public void onDisarmed() {
		showToast(R.string.toast_win, Toast.LENGTH_SHORT);
	}

	@Override
	public void onStart() {
		showToast(R.string.toast_longpress, Toast.LENGTH_SHORT);
	}

	private void runExplodeVibration() {
		final Vibrator vibrator = settings.getVibrator();
		if (vibrator != null)
			vibrator.vibrate(new long[] { 1, 50, 100, 150, 200, 300 }, -1);
	}

}
