package de.devisnik.android.mine.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.drawable.Drawable;

public class TouchDrawable extends Drawable {

	private Paint itsPaint;
	private Path itsCircle;
	private final int itsColor;

	public TouchDrawable(int color) {
		this.itsColor = color;
		itsPaint = new Paint();
		itsPaint.setAntiAlias(true);
		itsCircle = createCircle();
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		canvas.scale(getBounds().width() / 100f, getBounds().height() / 100f);
		itsPaint.setStyle(Paint.Style.FILL);
		itsPaint.setColor(itsColor);
		canvas.drawPath(itsCircle, itsPaint);
//		itsPaint.setStrokeWidth(5);
//		canvas.drawPath(itsPost, itsPaint);
		canvas.restore();
	}

	private static Path createCircle() {
		Path path = new Path();
		path.addCircle(50f, 50f, 40, Direction.CW);
		return path;
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
