package de.devisnik.android.mine;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.preference.Preference;
import android.util.AttributeSet;

import de.devisnik.android.mine.base.R;

public class BrowserPreference extends Preference {

	private final Uri url;

	public BrowserPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BrowserPreference);
		url = Uri.parse(a.getString(R.styleable.BrowserPreference_url)
				+ new MinesInfo(context).getQueryString());
		a.recycle();
	}

	@Override
	protected void onClick() {
		getContext().startActivity(new Intent(Intent.ACTION_VIEW, url));
	}
}
