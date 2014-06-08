package de.devisnik.android.mine.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.content.Context;
import de.devisnik.mine.GameFactory;
import de.devisnik.mine.IGame;

public class ReadGameCommand extends CacheFileCommand<IGame, FileInputStream> {

	public ReadGameCommand(Context context, String fileName) {
		super(context, fileName, null);
	}

	@Override
	IGame execute(FileInputStream stream) throws IOException {
		return GameFactory.readFromStream(stream);
	}

	@Override
	FileInputStream createStream(File file) throws IOException {
		return new FileInputStream(file);
	}

}
