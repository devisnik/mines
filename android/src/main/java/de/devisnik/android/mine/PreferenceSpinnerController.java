package de.devisnik.android.mine;

import java.util.Arrays;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Spinner;

public class PreferenceSpinnerController {
	private final String itsPrefKey;
	private final Spinner itsSpinner;
	private CharSequence[] itsValues;

	PreferenceSpinnerController(int prefKeyId, int valuesId, Spinner spinner) {
		itsSpinner = spinner;
		itsPrefKey = getContext().getString(prefKeyId);
		itsValues = getValues(valuesId);
		reset();
	}

	void reset() {
		SharedPreferences preferences = getPreferences();
		String oldValue = preferences.getString(itsPrefKey, "");
		int index = Arrays.asList(itsValues).indexOf(oldValue);
		itsSpinner.setSelection(index);
	}

	void updatePreference() {
		writePreference(itsPrefKey, itsValues[itsSpinner.getSelectedItemPosition()].toString());
	}

	private Context getContext() {
		return itsSpinner.getContext();
	}

	private void writePreference(String key, String value) {
		SharedPreferences preferences = getPreferences();
		String oldValue = preferences.getString(key, "");
		if (oldValue.equals(value))
			return;
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	private CharSequence[] getValues(int resId) {
		return getContext().getResources().getTextArray(resId);
	}

	private SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(getContext());
	}

}
