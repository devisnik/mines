package de.devisnik.android.mine.drawable;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;

public final class DrawableStore {

	private final SparseArray<Drawable> itsCache = new SparseArray<Drawable>();
	private final IDrawableConverter itsConverter;

	public DrawableStore(final IDrawableConverter converter) {
		itsConverter = converter;
	}

	public Drawable get(final int id) {
		return itsCache.get(id);
	}

	public Drawable put(final int id, final Drawable imageToCache) {
		Drawable converted = itsConverter.convert(imageToCache);
		itsCache.put(id, converted);
		return converted;
	}

	public boolean hasDrawableForId(final int id) {
		return itsCache.get(id) != null;
	}

	public void clear() {
		itsCache.clear();
	}

}
