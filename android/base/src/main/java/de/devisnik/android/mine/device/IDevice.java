package de.devisnik.android.mine.device;

import android.app.Activity;

public interface IDevice {

	boolean isGoogleTv();

	int getBuildVersion();

	void setFullScreen(Activity activity);

	void enableHomeButton(Activity activity);

	void setHighScoresTitle(Activity activity, CharSequence title, CharSequence subtitle);

	void setGameTitle(Activity activity, CharSequence title, CharSequence subtitle);
}
