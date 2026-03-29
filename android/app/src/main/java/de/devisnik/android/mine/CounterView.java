package de.devisnik.android.mine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * A custom TextView for digital counters that provides a stable layout width.
 *
 * This view is designed to prevent layout "ripple effects" in its parent container.
 * By maintaining a constant measured width (based on its maximum number of digits),
 * it ensures that siblings (like scrolling marquee text) are not resized or reset
 * when the counter's value changes.
 */
public final class CounterView extends TextView {

    private final int numberOfDigits;
    private final char[] digits;
    private final char[] measureChars;
    private boolean blockLayout;
    private int currentValue = Integer.MIN_VALUE;

    public CounterView(Context context, AttributeSet attribs) {
        super(context, attribs);
        TypedArray a = context.obtainStyledAttributes(attribs, R.styleable.CounterView);
        numberOfDigits = a.getInt(R.styleable.CounterView_digits, 3);
        String font = a.getString(R.styleable.CounterView_typeface);
        a.recycle();
        digits = new char[numberOfDigits];
        measureChars = new char[numberOfDigits];
        // Pre-fill measurement buffer with zeros to ensure stable width calculation.
        for (int i = 0; i < numberOfDigits; i++) {
            measureChars[i] = '0';
        }
        if (!isInEditMode())
            setTypeface(createTypeFace(context, font));
        setValue(0);
    }

    private Typeface createTypeFace(Context context, String font) {
        if (font == null)
            return Typeface.DEFAULT;
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    /**
     * Overridden to provide a stable width regardless of the current digits shown.
     * It measures the width of a string of '0' characters of length {@code numberOfDigits}.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // Only override if width is not explicitly set to an exact value in XML.
        if (widthMode != MeasureSpec.EXACTLY) {
            int width = (int) Math.ceil(getPaint().measureText(measureChars, 0, numberOfDigits))
                    + getPaddingLeft() + getPaddingRight();

            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, MeasureSpec.getSize(widthMeasureSpec));
            }
            setMeasuredDimension(width, getMeasuredHeight());
        }
    }

    /**
     * Overridden to allow suppression of layout requests.
     * This is used to prevent the view from triggering a full layout pass in its parent
     * when only the text content changes but the size is guaranteed to stay the same.
     */
    @Override
    public void requestLayout() {
        if (!blockLayout) {
            super.requestLayout();
        }
    }

    public void setValue(final int value) {
        // Optimization: skip updates if the value remains the same.
        if (value == currentValue) {
            return;
        }
        currentValue = value;
        for (int i = numberOfDigits - 1, remainder = value; i >= 0; i -= 1, remainder /= 10)
            digits[i] = Character.forDigit(remainder % 10, 10);
        
        // Block layout requests during setText to prevent marquee resets in siblings.
        blockLayout = true;
        setText(new String(digits));
        blockLayout = false;
    }
}
