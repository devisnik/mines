package de.devisnik.android.mine.device;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;

@TargetApi(11)
public class PostHoneycombDevice extends BaseDevice {

	private final Context mContext;

	PostHoneycombDevice(final Context context) {
		mContext = context;
	}

	@Override
	public boolean isGoogleTv() {
		try {
			PackageManager pm = mContext.getPackageManager();
			return pm.hasSystemFeature("com.google.android.tv");
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void setFullScreen(final Activity activity) {
		// no fullscreen when action bar is available
	}

	@TargetApi(14)
	@Override
	public void enableHomeButton(final Activity activity) {
		ActionBar actionBar = activity.getActionBar();
		if (getBuildVersion() >= 14 && actionBar != null)
			actionBar.setHomeButtonEnabled(true);

		// http://stackoverflow.com/questions/6867076/getactionbar-returns-null
		// actionbar may be null on API 11 and 12
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public void setHighScoresTitle(final Activity activity, final CharSequence title, final CharSequence subtitle) {
		ActionBar actionBar = activity.getActionBar();
		// http://stackoverflow.com/questions/6867076/getactionbar-returns-null
		// actionbar may be null on API 11 and 12
		if (actionBar != null) {
			actionBar.setTitle(title);
			actionBar.setSubtitle(subtitle);
		}
	}

	@Override
	public void setGameTitle(final Activity activity, final CharSequence title, final CharSequence subtitle) {
		if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			setHighScoresTitle(activity, title, subtitle);
		else {
			// http://stackoverflow.com/questions/6867076/getactionbar-returns-null
			// actionbar may be null on API 11 and 12
			ActionBar actionBar = activity.getActionBar();
			if (actionBar != null)
				actionBar.setTitle(title);
		}
	}
}
