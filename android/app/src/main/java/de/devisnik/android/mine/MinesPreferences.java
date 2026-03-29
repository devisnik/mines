package de.devisnik.android.mine;

import android.app.Activity;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import de.devisnik.android.mine.device.Device;

public class MinesPreferences extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);
        Device device = getDevice();
        device.setFullScreen(this);
        device.enableHomeButton(this);
    }

    public static class MinesPreferenceFragment extends PreferenceFragment {
        private final SummaryUpdater summaryUpdater = new SummaryUpdater();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            adjustListPreference(R.string.prefkey_board_theme);
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
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Device getDevice() {
        return ((MinesApplication) getApplication()).getDevice();
    }
}
