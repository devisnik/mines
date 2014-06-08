package de.devisnik.android.mine.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class ShadowDrawable extends Drawable {

	private static final int COLOR_DARK = 0x60000000;
	private static final int COLOR_LIGHT = 0x60ffffff;
	private static final int STROKE_WIDTH = 1;
	private Paint itsPaint;

	private static Path createDarkPath(int width, int height) {
		final Path path = new Path();
		path.moveTo(0, height);
		path.lineTo(width, height);
		path.lineTo(width, 0);
		return path;
	}

	private static Path createLightPath(int width, int height) {
		final Path path = new Path();
		path.moveTo(0, height);
		path.lineTo(0, 0);
		path.lineTo(width, 0);
		return path;
	}

	public ShadowDrawable() {
		itsPaint = new Paint();
		itsPaint.setStyle(Paint.Style.STROKE);
		itsPaint.setStrokeWidth(STROKE_WIDTH);
	}

	@Override
	public void draw(Canvas canvas) {
		Rect bounds = getBounds();
		itsPaint.setColor(COLOR_LIGHT);
		int width = bounds.width() - STROKE_WIDTH;
		int height = bounds.height() - STROKE_WIDTH;
		canvas.drawPath(createLightPath(width, height), itsPaint);
		itsPaint.setColor(COLOR_DARK);
		canvas.drawPath(createDarkPath(width, height), itsPaint);
	}

	@Override
	public int getOpacity() {
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}
}
