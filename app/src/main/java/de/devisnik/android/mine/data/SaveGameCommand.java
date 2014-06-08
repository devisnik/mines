package de.devisnik.android.mine.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import de.devisnik.mine.GameFactory;
import de.devisnik.mine.IGame;

public class SaveGameCommand extends CacheFileCommand<Boolean, FileOutputStream> {

	private final IGame itsGame;

	public SaveGameCommand(Context context, String fileName, IGame game) {
		super(context, fileName, Boolean.FALSE);
		itsGame = game;
	}

	@Override
	FileOutputStream createStream(File file) throws FileNotFoundException {
		return new FileOutputStream(file);
	}

	@Override
	Boolean execute(FileOutputStream stream) throws IOException {
		GameFactory.writeToStream(itsGame, stream);
		return Boolean.TRUE;
	}

}
