package de.devisnik.android.mine.drawable;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import de.devisnik.mine.Point;

public abstract class CachingDrawer {

	private final SparseArray<Drawable> itsDrawables = new SparseArray<Drawable>();
	private final DrawableStore itsStore = new DrawableStore(new ToBitmapConverter());
	private Point itsSize;

	public CachingDrawer(final int width, final int height) {
		itsSize = new Point(width, height);
	}

	public Point getSize() {
		return itsSize;
	}

	private Drawable get(final int id) {
		Drawable stored = itsStore.get(id);
		if (stored != null)
			return stored;
		stored = itsStore.put(id, prepareDrawable(id));
		if (stored == null)
			return itsDrawables.get(id);
		return stored;
	}

	private Drawable prepareDrawable(final int id) {
		Drawable drawable = itsDrawables.get(id);
		drawable.setBounds(0, 0, itsSize.x, itsSize.y);
		return drawable;
	}

	public void draw(final int id, final Canvas canvas) {
		get(id).draw(canvas);
	}

	public abstract void drawFocus(final Canvas canvas);

	public abstract void drawTouched(final Canvas canvas);

	protected void register(final int id, final Drawable drawable) {
		itsDrawables.put(id, drawable);
	}

	public void setSize(final int width, final int height) {
		if (itsSize.x == width && itsSize.y == height)
			return;
		itsSize = new Point(width, height);
		itsStore.clear();
	}
}
