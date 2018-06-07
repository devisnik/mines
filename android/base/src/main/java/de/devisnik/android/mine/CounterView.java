package de.devisnik.android.mine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import de.devisnik.android.mine.base.R;

public final class CounterView extends TextView {

    private final int numberOfDigits;
    private final char[] digits;

    public CounterView(Context context, AttributeSet attribs) {
        super(context, attribs);
        TypedArray a = context.obtainStyledAttributes(attribs, R.styleable.CounterView);
        numberOfDigits = a.getInt(R.styleable.CounterView_digits, 3);
        String font = a.getString(R.styleable.CounterView_typeface);
        a.recycle();
        digits = new char[numberOfDigits];
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
        for (int i = numberOfDigits - 1, remainder = value; i >= 0; i -= 1, remainder /= 10)
            digits[i] = Character.forDigit(remainder % 10, 10);
        setText(new String(digits));
    }
}
