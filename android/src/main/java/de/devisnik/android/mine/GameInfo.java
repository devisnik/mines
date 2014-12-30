package de.devisnik.android.mine;

import java.util.Arrays;

import de.devisnik.mine.IGame;

public class GameInfo {
	private final Settings itsSettings;

	public GameInfo(Settings settings) {
		itsSettings = settings;
	}

	private String getEntryForValue(int entryArrayId, int valueArrayId, String value) {
		String[] strings = getStringArray(valueArrayId);
		int valueIndex = Arrays.asList(strings).indexOf(value);
		return getStringArray(entryArrayId)[valueIndex];
	}

	public String createTitle() {
		StringBuilder builder = new StringBuilder();
		builder.append(getString(R.string.level) + ":"
				+ getEntryForValue(R.array.levels, R.array.levels_values, itsSettings.getLevel()));
		builder.append(", ");
		builder.append(getString(R.string.board) + ":"
				+ getEntryForValue(R.array.sizes, R.array.sizes_values, itsSettings.getBoard()));
		return builder.toString();
	}

	public String createStatus(IGame game) {
		int flagsToSet = game.getBombCount() - game.getBoard().getFlagCount();
		return flagsToSet + " Flags left to set after "
				+ game.getWatch().getTime() + " seconds.";
	}

	private String getString(int id) {
		return itsSettings.getString(id);
	}

	private String[] getStringArray(int id) {
		return itsSettings.getStringArray(id);
	}

}
