package de.devisnik.android.mine.data;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public abstract class CacheFileCommand<RESULT, STREAM extends Closeable> {
	
	private static final String TAG = CacheFileCommand.class.getSimpleName();
	private final RESULT itsErrorResult;
	private final Context itsContext;
	private final String itsFileName;
	
	public CacheFileCommand(Context context, String fileName, RESULT errorResult) {
		itsContext = context;
		itsFileName = fileName;
		itsErrorResult = errorResult;			
	}
	
	protected final File createCacheFile() {
		return new File(itsContext.getCacheDir(), itsFileName);
	}
	
	abstract STREAM createStream(File file) throws IOException;
	
	public final RESULT execute() {
		STREAM stream = null;
		try {
			stream = createStream(createCacheFile());
			return execute(stream);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			return itsErrorResult;
		} finally {
			if (stream != null)
				try {
					stream.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
				}
		}

	}
	abstract RESULT execute(STREAM stream) throws IOException;
}
