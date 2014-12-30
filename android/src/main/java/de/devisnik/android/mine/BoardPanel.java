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
	private int itsDimX;
	private int itsDimY;
	private CachingDrawer itsFieldDrawer;
	private int itsZoomModeFieldSize = -1;
	private int itsFieldSize;
	private boolean itsFitInParent;
	private final StyleColorProvider itsColorProvider;
	private FieldView[][] itsFields;

	public BoardPanel(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		final TypedArray ar = context.obtainStyledAttributes(attrs, R.styleable.BoardPanel);
		itsColorProvider = new StyleColorProvider(ar);
		ar.recycle();
		if (!isInEditMode())
			itsFieldDrawer = new FieldDrawer(itsColorProvider, null);
		else {
			itsFieldDrawer = new PreviewFieldDrawer(itsColorProvider, null);
			setDimension(10, 10);
			itsZoomModeFieldSize = 20;
		}
	}

	public void setDimension(final int dimX, final int dimY) {
		itsDimX = dimX;
		itsDimY = dimY;
		if (getChildCount() == 0)
			createFields();
	}

	public int getDimensionX() {
		return itsDimX;
	}

	public int getDimensionY() {
		return itsDimY;
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
		itsZoomModeFieldSize = size;
        requestLayout();
	}

	public int getFieldSize() {
		return itsFieldSize;
	}

	private void createFields() {
		itsFields = new FieldView[itsDimX][itsDimY];
		for (int posY = 0; posY < itsDimY; posY++)
			for (int posX = 0; posX < itsDimX; posX++) {
				FieldView field = createField();
				// we can not rely on the child view order for positioning since
				// this may change due to
				// animation visibility issues (we need to bringToFront the
				// field view first)
				// So we put the target position into a tag on the field view
				field.setTag(R.id.FIELD_POSITION, new Point(posX, posY));
				addView(field);
				itsFields[posX][posY] = field;
			}
		wireFocusBorders();
	}

	private void wireFocusBorders() {
		for (int row = 0; row < itsDimY; row++) {
			final FieldView left = getField(0, row);
			final FieldView right = getField(itsDimX - 1, row);
			left.setNextFocusLeftId(right.getId());
			right.setNextFocusRightId(left.getId());
		}
		for (int column = 0; column < itsDimX; column++) {
			final FieldView top = getField(column, 0);
			final FieldView bottom = getField(column, itsDimY - 1);
			top.setNextFocusUpId(bottom.getId());
			bottom.setNextFocusDownId(top.getId());
		}
	}

	public FieldView getField(final int posX, final int posY) {
		return itsFields[posX][posY];
	}

	private FieldView createField() {
		final FieldView fieldView = new FieldView(getContext(), itsFieldDrawer);
		fieldView.setId(theFieldId);
		theFieldId++;
		return fieldView;
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
		Log.i("Mines.render", "BoardPanel.onMeasure()");
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
		int fitSizeX = measureWidth / itsDimX;
		int fitSizeY = measureHeight / itsDimY;
		int fieldSizeInFitMode = Math.min(fitSizeX, fitSizeY);
		int fieldSizeInOneDimensionFitMode = Math.max(fitSizeX, fitSizeY);

		// always fill at least the available screen
		itsFieldSize = Math.max(itsZoomModeFieldSize, fieldSizeInFitMode);
		itsFitInParent = itsFieldSize == fieldSizeInFitMode;
		setFieldsFocusable(itsFitInParent);

		// if only one side needs scrollbar, fill the other side
		if (!itsFitInParent && itsFieldSize < fieldSizeInOneDimensionFitMode)
			itsFieldSize = fieldSizeInOneDimensionFitMode;

		setMeasuredDimension(itsFieldSize * itsDimX, itsFieldSize * itsDimY);
	}

	public boolean fitsIntoParent() {
		return itsFitInParent;
	}

	/*
	 * We can not rely on the child order for positioning due to animation
	 * problems which need view reordering to be resolved, see createFields().
	 */
	@Override
	protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
		Log.i("Mines.render", "BoardPanel.onLayout()");
		for (int posX = 0; posX < itsDimX; posX++)
			for (int posY = 0; posY < itsDimY; posY++)
				itsFields[posX][posY].layout(posX * itsFieldSize, posY * itsFieldSize, (posX + 1) * itsFieldSize,
						(posY + 1) * itsFieldSize);
	}

}
