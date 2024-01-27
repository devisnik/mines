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

    private void showToast(int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBusted() {
        runExplodeVibration();
        showToast(R.string.toast_lose);
    }

    @Override
    public void onChange(final int flags, final int mines) {
    }

    @Override
    public void onDisarmed() {
        showToast(R.string.toast_win);
    }

    @Override
    public void onStart() {
        showToast(R.string.toast_longpress);
    }

    @Override
    public void onClickAfterFinished() {
    }

    private void runExplodeVibration() {
        final Vibrator vibrator = settings.getVibrator();
        if (vibrator != null)
            vibrator.vibrate(new long[]{1, 50, 100, 150, 200, 300}, -1);
    }

}
