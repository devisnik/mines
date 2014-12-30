package de.devisnik.android.mine.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;

public class NumberDrawable extends Drawable {

	private String itsDigit;
	private Paint itsPaint;

	public NumberDrawable(int digit, int color) {
		this(digit, color, null);
	}

	public NumberDrawable(int digit, int color, Typeface face) {
		itsPaint = new Paint();
		itsPaint.setAntiAlias(true);
		itsPaint.setColor(color);
		itsPaint.setTextAlign(Align.CENTER);
		itsPaint.setTypeface(face);
		itsDigit = Integer.toString(digit);
	}

	@Override
	public void draw(Canvas canvas) {
		itsPaint.setTextSize(getBounds().height());
		canvas.drawText(itsDigit, getBounds().exactCenterX(),
				getBounds().bottom
						- (getBounds().bottom + itsPaint.ascent() + itsPaint
								.descent()) / 2, itsPaint);
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
