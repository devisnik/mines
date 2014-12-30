package de.devisnik.android.mine.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

public class FlagDrawable extends Drawable {

	private Paint itsPaint;
	private Path itsFlag;
	private Path itsPost;
	private final int itsFlagColor;
	private final int itsPoleColor;
	private final int itsFlagColorDark;

	public FlagDrawable(int flagColorLight, int flagColorDark, int poleColor) {
		this.itsFlagColor = flagColorLight;
		itsFlagColorDark = flagColorDark;
		this.itsPoleColor = poleColor;
		itsPaint = new Paint();
		itsPaint.setAntiAlias(true);
		itsFlag = createFlagPath();
		itsPost = createPath();
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		canvas.scale(getBounds().width() / 100f, getBounds().height() / 100f);
		itsPaint.setStyle(Paint.Style.FILL);
		itsPaint.setColor(itsFlagColor);
		itsPaint.setShader(new LinearGradient(57, 45, 25, 45, itsFlagColorDark, itsFlagColor, TileMode.CLAMP));
		canvas.drawPath(itsFlag, itsPaint);
		itsPaint.setStyle(Paint.Style.STROKE);
		itsPaint.setShader(null);
		itsPaint.setColor(itsPoleColor);
		itsPaint.setStrokeWidth(5);
		canvas.drawPath(itsPost, itsPaint);
		canvas.restore();
	}

	private static Path createFlagPath() {
		Path path = new Path();
		path.moveTo(57, 45);
		path.lineTo(57, 20);
		path.lineTo(25, 20);
		path.lineTo(25, 45);
		path.lineTo(55, 45);
		return path;
	}

	private static Path createPath() {
		Path path = new Path();
		path.moveTo(40, 82);
		path.quadTo(55, 70, 70, 82);
		path.close();
		path.moveTo(55, 75);
		path.lineTo(55, 45);
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
