package de.devisnik.android.mine.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

public class BombDrawable extends Drawable {

	private Paint itsPaint;
	private Path itsCross;
	private Path itsX;

	public BombDrawable(int innerColor, int outerColor) {
		super();
		itsPaint = new Paint();
		itsPaint.setShader(new RadialGradient(60, 60, 40, innerColor, outerColor, TileMode.CLAMP));
		itsPaint.setColor(innerColor);
		itsPaint.setAntiAlias(true);
		itsPaint.setStrokeCap(Cap.ROUND);
		itsCross = createCross();
		itsX = createX(28);
	}

	private static Path createCross() {
		Path path = new Path();
		path.moveTo(50, 15);
		path.lineTo(50, 85);
		path.moveTo(15, 50);
		path.lineTo(85, 50);
		return path;
	}

	private Path createX(int dist) {
		Path path = new Path();
		path.moveTo(dist, dist);
		path.lineTo(100-dist, 100-dist);
		path.moveTo(dist, 100-dist);
		path.lineTo(100-dist, dist);
		return path;	
	}
	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		int width = getBounds().width();
		int height = getBounds().height();
//		int min = Math.min(width, height);
		canvas.scale(width/100f, height/100f);
		itsPaint.setStyle(Paint.Style.STROKE);
		itsPaint.setStrokeWidth(5f);
		canvas.drawPath(itsCross, itsPaint);
		itsPaint.setStyle(Paint.Style.STROKE);
		itsPaint.setStrokeWidth(3.5f);
		canvas.drawPath(itsX, itsPaint);
		itsPaint.setStyle(Paint.Style.FILL);
		canvas.drawOval(new RectF(30,30,70,70), itsPaint);
		canvas.restore();
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
