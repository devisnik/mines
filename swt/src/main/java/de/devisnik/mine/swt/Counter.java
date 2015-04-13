package de.devisnik.mine.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class Counter extends Composite {
    private final MinesImages images;
    private final int maxvalue;
    private final Label[] digits;
    private int value;

    public Counter(final Composite parent, final int style, final int length,
                   final int value, final MinesImages images) {
        super(parent, style);
        this.value = value;
        this.images = images;
        digits = new Label[length];
        maxvalue = power(10, length) - 1;

        // Set layout.
        final RowLayout counterLayout = new RowLayout();
        counterLayout.justify = true;
        counterLayout.spacing = 0;
        counterLayout.marginTop = 0;
        counterLayout.marginBottom = 0;
        counterLayout.marginLeft = 0;
        counterLayout.marginRight = 0;
        this.setLayout(counterLayout);

        // Initialize the counter.
        for (int i = 0; i < length; i++) {
            digits[i] = new Label(this, SWT.NONE);
            digits[i].setBackground(Display.getCurrent().getSystemColor(
                    SWT.COLOR_BLACK));
        }
        drawCounter(value);
    }

    private static int power(int number, int exponent) {
        int pow = 1;
        for (int i = 1; i <= exponent; i++) {
            pow *= number;
        }
        return pow;
    }

    public void drawCounter(final int param) {
        int position = digits.length - 1;
        int value = param;
        while (position >= 0) {
            digits[position].setImage(images.getCounterImages()[value % 10]);
            position--;
            value /= 10;
        }
    }

    public void increase() {
        value += 1;
        if (!(value > maxvalue)) {
            drawCounter(value);
        }
    }

    public void increase(final int param) {
        value += param;
        if (value > maxvalue) {
            drawCounter(maxvalue);
        } else {
            drawCounter(value);
        }
    }

    public void decrease() {
        value -= 1;
        drawCounter(value);
    }

    public void reset() {
        value = 0;
        drawCounter(value);
    }

    public void reset(final int value) {
        this.value = value;
        drawCounter(value);
    }

    public int getValue() {
        return value;
    }

    public boolean isZero() {
        return value == 0;
    }
}
