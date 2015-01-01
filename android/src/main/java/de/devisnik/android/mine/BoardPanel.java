package de.devisnik.android.mine;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import de.devisnik.android.mine.drawable.CachingDrawer;
import de.devisnik.android.mine.drawable.FieldDrawer;
import de.devisnik.android.mine.drawable.PreviewFieldDrawer;
import de.devisnik.android.mine.drawable.StyleColorProvider;
import de.devisnik.mine.Point;

public class BoardPanel extends ViewGroup {
	private static int theFieldId;
	private int dimX;
	private int dimY;
	private CachingDrawer fieldDrawer;
	private int zoomModeFieldSize = -1;
	private int fieldSize;
	private boolean isFitInParent;
    private FieldView[][] fieldViews;

	public BoardPanel(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		final TypedArray ar = context.obtainStyledAttributes(attrs, R.styleable.BoardPanel);
        StyleColorProvider itsColorProvider = new StyleColorProvider(ar);
		ar.recycle();
		if (!isInEditMode())
			fieldDrawer = new FieldDrawer(itsColorProvider, null);
		else {
			fieldDrawer = new PreviewFieldDrawer(itsColorProvider, null);
			setDimension(10, 10);
			zoomModeFieldSize = 20;
		}
	}

	public void setDimension(final int dimX, final int dimY) {
		this.dimX = dimX;
		this.dimY = dimY;
		if (getChildCount() == 0)
			createFields();
	}

	public int getDimensionX() {
		return dimX;
	}

	public int getDimensionY() {
		return dimY;
	}

	private void setFieldsFocusable(final boolean value) {
		int childCount = getChildCount();
		for (int index = 0; index < childCount; index++)
			getChildAt(index).setFocusable(value);
	}

	public void setFieldsFocusableInTouchMode(final boolean value) {
		int childCount = getChildCount();
		for (int index = 0; index < childCount; index++)
			getChildAt(index).setFocusableInTouchMode(value);
	}

	public void setZoomModeFieldSize(final int size) {
		zoomModeFieldSize = size;
        requestLayout();
	}

	public int getFieldSize() {
		return fieldSize;
	}

	private void createFields() {
		fieldViews = new FieldView[dimX][dimY];
		for (int posY = 0; posY < dimY; posY++)
			for (int posX = 0; posX < dimX; posX++) {
				FieldView field = createField();
				// we can not rely on the child view order for positioning since
				// this may change due to
				// animation visibility issues (we need to bringToFront the
				// field view first)
				// So we put the target position into a tag on the field view
				field.setTag(R.id.FIELD_POSITION, new Point(posX, posY));
				addView(field);
				fieldViews[posX][posY] = field;
			}
		wireFocusBorders();
	}

	private void wireFocusBorders() {
		for (int row = 0; row < dimY; row++) {
			final FieldView left = getField(0, row);
			final FieldView right = getField(dimX - 1, row);
			left.setNextFocusLeftId(right.getId());
			right.setNextFocusRightId(left.getId());
		}
		for (int column = 0; column < dimX; column++) {
			final FieldView top = getField(column, 0);
			final FieldView bottom = getField(column, dimY - 1);
			top.setNextFocusUpId(bottom.getId());
			bottom.setNextFocusDownId(top.getId());
		}
	}

	public FieldView getField(final int posX, final int posY) {
		return fieldViews[posX][posY];
	}

	private FieldView createField() {
		final FieldView fieldView = new FieldView(getContext(), fieldDrawer);
		fieldView.setId(theFieldId);
		theFieldId++;
		return fieldView;
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
		Log.i("Mines.render", "BoardPanel.onMeasure()");
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
		int fitSizeX = measureWidth / dimX;
		int fitSizeY = measureHeight / dimY;
		int fieldSizeInFitMode = Math.min(fitSizeX, fitSizeY);
		int fieldSizeInOneDimensionFitMode = Math.max(fitSizeX, fitSizeY);

		// always fill at least the available screen
		fieldSize = Math.max(zoomModeFieldSize, fieldSizeInFitMode);
		isFitInParent = fieldSize == fieldSizeInFitMode;
		setFieldsFocusable(isFitInParent);

		// if only one side needs scrollbar, fill the other side
		if (!isFitInParent && fieldSize < fieldSizeInOneDimensionFitMode)
			fieldSize = fieldSizeInOneDimensionFitMode;

		setMeasuredDimension(fieldSize * dimX, fieldSize * dimY);
	}

	public boolean fitsIntoParent() {
		return isFitInParent;
	}

	/*
	 * We can not rely on the child order for positioning due to animation
	 * problems which need view reordering to be resolved, see createFields().
	 */
	@Override
	protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
		Log.i("Mines.render", "BoardPanel.onLayout()");
		for (int posX = 0; posX < dimX; posX++)
			for (int posY = 0; posY < dimY; posY++)
				fieldViews[posX][posY].layout(posX * fieldSize, posY * fieldSize, (posX + 1) * fieldSize,
						(posY + 1) * fieldSize);
	}

}
