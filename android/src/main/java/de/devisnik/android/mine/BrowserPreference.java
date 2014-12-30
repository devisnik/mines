package de.devisnik.android.mine;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.preference.Preference;
import android.util.AttributeSet;

public class BrowserPreference extends Preference {

	private final Uri itsURL;

	public BrowserPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BrowserPreference);
		itsURL = Uri.parse(a.getString(R.styleable.BrowserPreference_url)
				+ new MinesInfo(context).getQueryString());
		a.recycle();
	}

	@Override
	protected void onClick() {
		getContext().startActivity(new Intent(Intent.ACTION_VIEW, itsURL));
	}
}
