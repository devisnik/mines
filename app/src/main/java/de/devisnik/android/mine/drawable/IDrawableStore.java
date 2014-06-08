package de.devisnik.android.mine.drawable;

import android.graphics.drawable.Drawable;

public interface IDrawableStore {
	void put(int id, Drawable imageToCache);
	Drawable get(int id);
}
