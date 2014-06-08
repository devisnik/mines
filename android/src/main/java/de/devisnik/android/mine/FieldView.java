package de.devisnik.android.mine;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import de.devisnik.android.mine.drawable.CachingDrawer;

final class FieldView extends View {

	private final CachingDrawer itsFieldDrawer;
	private int itsImageId;
	private boolean itsTouched;

	public FieldView(final Context context) {
		this(context, null);
	}

	FieldView(final Context context, final CachingDrawer fieldDrawer) {
		super(context);
		this.itsFieldDrawer = fieldDrawer;
		setHapticFeedbackEnabled(false);
		setLongClickable(true);
		if (isInEditMode())
			itsImageId = 10;
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		super.onDraw(canvas);
		if (itsFieldDrawer == null)
			return;
		itsFieldDrawer.draw(itsImageId, canvas);
		if (isFocused())
			itsFieldDrawer.drawFocus(canvas);
		if (itsTouched)
			itsFieldDrawer.drawTouched(canvas);
	}

	public void setImageId(final int id) {
		itsImageId = id;
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		itsFieldDrawer.setSize(w, h);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(200, 200);
	}

	public void setTouched(final boolean touched) {
		itsTouched = touched;
	}
}