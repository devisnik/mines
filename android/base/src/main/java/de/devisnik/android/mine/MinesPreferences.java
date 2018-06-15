package de.devisnik.android.mine;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import de.devisnik.android.mine.device.IDevice;

public class MinesPreferences extends PreferenceActivity {

	private final SummaryUpdater summaryUpdater = new SummaryUpdater();

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getDevice().enableHomeButton(this);
		addPreferencesFromResource(R.xml.preferences);

		adjustListPreference(R.string.prefkey_board_theme);
		adjustListPreference(R.string.prefkey_field_size);
		adjustStringPreference(R.string.prefkey_user_name);
	}

	private void adjustListPreference(final int key) {
		final ListPreference preference = (ListPreference) findPreference(getString(key));
		summaryUpdater.onPreferenceChange(preference, preference.getValue());
		preference.setOnPreferenceChangeListener(summaryUpdater);
	}

	private void adjustStringPreference(final int key) {
		final EditTextPreference preference = (EditTextPreference) findPreference(getString(key));
		final String value = preference.getText();
		summaryUpdater
				.onPreferenceChange(preference, value == null ? getString(R.string.pref_value_not_set) : value);
		preference.setOnPreferenceChangeListener(summaryUpdater);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			startActivity(new Intent(this, MineSweeper.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private IDevice getDevice() {
		return ((MinesApplication) getApplication()).getDevice();
	}
}
