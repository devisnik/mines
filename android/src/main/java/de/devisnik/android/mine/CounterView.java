package de.devisnik.android.mine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CounterView extends TextView {

	private final int itsLength;
	private final char[] itsDigits;
	private int itsValue;

	public CounterView(Context context, AttributeSet attribs) {
		super(context, attribs);
		TypedArray a = context.obtainStyledAttributes(attribs, R.styleable.CounterView);
		itsLength = a.getInt(R.styleable.CounterView_digits, 3);
		String font = a.getString(R.styleable.CounterView_typeface);
		a.recycle();
		itsDigits = new char[itsLength];
		if (!isInEditMode())
			setTypeface(createTypeFace(context, font));
		setValue(0);
	}

	private Typeface createTypeFace(Context context, String font) {
		if (font == null)
			return Typeface.DEFAULT;
		return Typeface.createFromAsset(context.getAssets(), font);
	}

	public void setValue(final int value) {
		itsValue = value;
		int internalValue = value;
		for (int i = 0; i < itsLength; i++) {
			itsDigits[itsLength - 1 - i] = Character.forDigit(internalValue % 10, 10);
			internalValue /= 10;
		}
		setText(new String(itsDigits));
	}

	/**
	 * For testing only!
	 * 
	 * @return the value currently displayed
	 */
	public int getValue() {
		return itsValue;
	}
}
