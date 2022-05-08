package de.devisnik.android.mine;

import java.util.Arrays;

import de.devisnik.mine.IGame;

public final class GameInfo {

	private final Settings settings;

	public GameInfo(Settings settings) {
		this.settings = settings;
	}

	private String getEntryForValue(int entryArrayId, int valueArrayId, String value) {
		String[] strings = getStringArray(valueArrayId);
		int valueIndex = Arrays.asList(strings).indexOf(value);
		return getStringArray(entryArrayId)[valueIndex];
	}

	public String createTitle() {
		return getString(R.string.level) + ":" + getEntryForValue(R.array.levels, R.array.levels_values, settings.getLevel())
				+ ", "
				+ getString(R.string.board) + ":" + getEntryForValue(R.array.sizes, R.array.sizes_values, settings.getBoard());
	}

	public String createStatus(IGame game) {
		int flagsToSet = game.getBombCount() - game.getBoard().getFlagCount();
		return flagsToSet + " Flags left to set after "
				+ game.getWatch().getTime() + " seconds.";
	}

	private String getString(int id) {
		return settings.getString(id);
	}

	private String[] getStringArray(int id) {
		return settings.getStringArray(id);
	}

}
