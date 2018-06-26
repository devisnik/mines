package de.devisnik.android.mine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

public final class Settings {
    private static final String DEFAULT_USER_NAME = "";
    private static final int DEFAULT_BOARD = R.string.board_value_medium;
    private static final int DEFAULT_LEVEL = R.string.level_value_easy;
    private static final boolean DEFAULT_BOARD_FIT = true;

    private final SharedPreferences preferences;
    private final Resources resources;
    private final Context context;

    public Settings(final Context context) {
        this.context = context;
        resources = context.getResources();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        ensureNonUiPreferenceDefaults();
    }

    /**
     * The preferences not defined in preferences.xml have to be initialized on first use here.
     */
    private void ensureNonUiPreferenceDefaults() {
        ensureIsInitializedBooleanPreference(R.string.prefkey_board_fit, DEFAULT_BOARD_FIT);
        ensureIsInitializedStringPreference(R.string.prefkey_board_size, DEFAULT_BOARD);
        ensureIsInitializedStringPreference(R.string.prefkey_game_level, DEFAULT_LEVEL);
    }

    public String getString(final int id) {
        return resources.getString(id);
    }

    public String[] getStringArray(final int id) {
        return resources.getStringArray(id);
    }

    private String getStringPreference(final int id, final String defValue) {
        return preferences.getString(getString(id), defValue);
    }

    private int getIntPreference(final int id, final int defValue) {
        return preferences.getInt(getString(id), defValue);
    }

    private boolean getBooleanPreference(final int id, final boolean defValue) {
        return preferences.getBoolean(getString(id), defValue);
    }

    private void ensureIsInitializedBooleanPreference(final int id, final boolean defaultValue) {
        if (preferences.contains(getString(id))) {
            return;
        }
        writeBoolean(id, defaultValue);
    }

    private void ensureIsInitializedStringPreference(final int id, final int defaultValueId) {
        if (preferences.contains(getString(id))) {
            return;
        }
        writeString(id, getString(defaultValueId));
    }

    public int getFieldSize() {
        if (!isZoom()) {
            // TODO handle fit mode better
            return -1;
        }
        return getZoomFieldSize();
    }

    public int getZoomFieldSize() {
        return resources.getDimensionPixelSize(R.dimen.field_size_large);
    }

    public String getLevel() {
        return getStringPreference(R.string.prefkey_game_level, getString(R.string.level_value_easy));
    }

    public String getBoard() {
        return getStringPreference(R.string.prefkey_board_size, getString(R.string.board_value_small));
    }

    public int[] getBoardDimension() {
        String board = getBoard();
        if (equals(R.string.board_value_small, board)) {
            return resources.getIntArray(R.array.board_size_small);
        }
        if (equals(R.string.board_value_medium, board)) {
            return resources.getIntArray(R.array.board_size_medium);
        }
        if (equals(R.string.board_value_large, board)) {
            return resources.getIntArray(R.array.board_size_large);
        }
        if (equals(R.string.board_value_huge, board)) {
            return resources.getIntArray(R.array.board_size_huge);
        }
        if (equals(R.string.board_value_giant, board)) {
            return resources.getIntArray(R.array.board_size_giant);
        }
        throw new IllegalStateException("unknown board size: " + board);
    }

    public Vibrator getVibrator() {
        Log.d("Settings", "vibration on? " + isVibrate());
        if (isVibrate()) {
            return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        return null;
    }

    public int getBombsNumber(final int fields) {
        String level = getLevel();
        if (equals(R.string.level_value_easy, level)) {
            return Math.round(fields * .1f);
        }
        if (equals(R.string.level_value_medium, level)) {
            return Math.round(fields * .12f);
        }
        if (equals(R.string.level_value_hard, level)) {
            return Math.round(fields * .15f);
        }
        if (equals(R.string.level_value_tricky, level)) {
            return Math.round(fields * .2f);
        }
        if (equals(R.string.level_value_insane, level)) {
            return Math.round(fields * .25f);
        }
        throw new IllegalStateException("unknown level: " + level);
    }

    private boolean equals(final int id, final String value) {
        return getString(id).equals(value);
    }

    public int getTheme() {
        String themePreference = getStringPreference(R.string.prefkey_board_theme, null);
        if (equals(themePreference, R.string.theme_value_classic)) {
            return R.style.Classic;
        }
        if (equals(themePreference, R.string.theme_value_simple)) {
            return R.style.Monochrome;
        }
        if (equals(themePreference, R.string.theme_value_dark)) {
            return R.style.Dark;
        }
        throw new IllegalStateException("unknown theme: " + themePreference);

    }

    private boolean equals(final String string, final int resourceId) {
        return string.equals(getString(resourceId));
    }

    public boolean isTouchHighlight() {
        return getBooleanPreference(R.string.prefkey_board_highlight, true);
    }

    public boolean isZoom() {
        return !getBooleanPreference(R.string.prefkey_board_fit, true);
    }

    public void toogleZoom() {
        writeBoolean(R.string.prefkey_board_fit, isZoom());
    }

    private void writeBoolean(final int id, final boolean value) {
        String key = getString(id);
        final Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private void writeString(final int id, final String value) {
        String key = getString(id);
        final Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void writeInt(final int id, final int value) {
        String key = getString(id);
        final Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public String getUserName() {
        return getStringPreference(R.string.prefkey_user_name, DEFAULT_USER_NAME);
    }

    private boolean isSetUserName() {
        return preferences.contains(getString(R.string.prefkey_user_name));
    }

    private boolean isInvalidUserName(final String value) {
        return value.length() == 0;
    }

    public int getLastUsedBuild() {
        return getIntPreference(R.string.prefkey_game_intro_shown, 0);
    }

    public void setLastUsedBuild(final int buildNumber) {
        writeInt(R.string.prefkey_game_intro_shown, buildNumber);
    }

    public boolean isVibrate() {
        return getBooleanPreference(R.string.prefkey_board_vibrate, true);
    }

    public boolean isAnimate() {
        return getBooleanPreference(R.string.prefkey_board_animate, true);
    }

    public void setUserNameIfNotSet(final String value) {
        if (isSetUserName() || isInvalidUserName(value)) {
            return;
        }
        writeString(R.string.prefkey_user_name, value);
    }

    public boolean isNotify() {
        return getBooleanPreference(R.string.prefkey_game_notification, false);
    }
}
