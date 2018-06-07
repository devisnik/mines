package de.devisnik.android.mine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Spinner;

import java.util.Arrays;

public class PreferenceSpinnerController {
	private final String prefKey;
	private final Spinner spinner;
	private CharSequence[] values;

	PreferenceSpinnerController(int prefKeyId, int valuesId, Spinner spinner) {
		this.spinner = spinner;
		prefKey = getContext().getString(prefKeyId);
		values = getValues(valuesId);
		reset();
	}

	void reset() {
		SharedPreferences preferences = getPreferences();
		String oldValue = preferences.getString(prefKey, "");
		int index = Arrays.asList(values).indexOf(oldValue);
		spinner.setSelection(index);
	}

	void updatePreference() {
		writePreference(prefKey, values[spinner.getSelectedItemPosition()].toString());
	}

	private Context getContext() {
		return spinner.getContext();
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
