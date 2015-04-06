package de.devisnik.mine.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import java.util.HashMap;

public class Counter extends Composite {
    private int value = 0;
    private int maxvalue = 9;
    private Label[] digits = null;

    private final HashMap<String, Image> itsImageMap;

    // private MinesImages itsMinesImages;

    public Counter(final Composite parent, final int style, final int lenght,
                   final int value, final String owner) {
        super(parent, style);
        this.value = value;

        this.digits = new Label[lenght];

        // Calculate maximum value.
        final StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < lenght; i++)
            tmp.append(9);
        maxvalue = Integer.parseInt(tmp.toString());

        // Set layout.
        final RowLayout counterLayout = new RowLayout();
        counterLayout.justify = true;
        counterLayout.spacing = 0;
        counterLayout.marginTop = 0;
        counterLayout.marginBottom = 0;
        counterLayout.marginLeft = 0;
        counterLayout.marginRight = 0;
        this.setLayout(counterLayout);

        itsImageMap = new HashMap();
        final MinesImages minesImages = new MinesImages(parent.getDisplay());
        for (int i = 0; i < 10; i++) {
            itsImageMap.put("" + i, minesImages.getCounterImages()[i]);
        }

        // Initialize the counter.
        for (int i = 0; i < lenght; i++) {
            digits[i] = new Label(this, SWT.NONE);
            digits[i].setBackground(Display.getCurrent().getSystemColor(
                    SWT.COLOR_BLACK));
        }
        drawCounter(value);
    }

    public void drawCounter(final int param) {

        // Calculate the images to be displayed.
        String aux = Integer.toString(param);
        final StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < digits.length - aux.length(); i++)
            tmp.append(0);
        tmp.append(aux);
        aux = tmp.toString();

        // Show images.

        for (int i = 0; i < digits.length; i++) {
            digits[i].setImage((Image) itsImageMap.get(aux.substring(i, i + 1)));
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
