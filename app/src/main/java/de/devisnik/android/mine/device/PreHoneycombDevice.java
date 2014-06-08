package de.devisnik.android.mine.device;

import android.app.Activity;
import android.view.Window;

public final class PreHoneycombDevice extends BaseDevice {

	/**
	 * package visible construction only.
	 */
	PreHoneycombDevice() {
	}

	@Override
	public boolean isGoogleTv() {
		return false;
	}

	@Override
	public void setFullScreen(final Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	public void enableHomeButton(final Activity activity) {
		// noop since no action bar
	}

	@Override
	public void setHighScoresTitle(Activity activity, CharSequence title, CharSequence subtitle) {
		activity.setTitle(title + " - " + subtitle);
	}

	@Override
	public void setGameTitle(Activity activity, CharSequence title, CharSequence subtitle) {
		setHighScoresTitle(activity, title, subtitle);
	}
}
