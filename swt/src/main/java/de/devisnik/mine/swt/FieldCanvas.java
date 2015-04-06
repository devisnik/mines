package de.devisnik.mine.swt;

import de.devisnik.mine.IField;
import de.devisnik.mine.IFieldListener;
import de.devisnik.mine.SimpleFieldListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class FieldCanvas extends Canvas {

    private static final Point SIZE = new Point(24, 24);
    private final IField itsField;
    private final IFieldListener itsPropertyChangeListener;


    public FieldCanvas(final Composite parent, final IField field, final MinesImages images) {
        super(parent, SWT.NO_BACKGROUND & SWT.NO_REDRAW_RESIZE);
        this.itsField = field;
        addPaintListener(new PaintListener() {
            public void paintControl(final PaintEvent e) {
                int imageId = itsField.getImage();
                Image image = images.getFieldImages()[imageId];
                Rectangle bounds = image.getBounds();
                e.gc.drawImage(image, 0, 0, bounds.width, bounds.height, 0, 0, SIZE.x, SIZE.y);
                if (itsField.isTouched())
                    e.gc.drawRoundRectangle(1, 1, SIZE.x - 3, SIZE.y - 3, 3, 3);
            }
        });
        itsPropertyChangeListener = new SimpleFieldListener() {
            @Override
            protected void onChange(IField field) {
                if (!isDisposed())
                    redraw();
            }
        };
        itsField.addListener(itsPropertyChangeListener);

    }

    public Point computeSize(final int hint, final int hint2,
                             final boolean changed) {
        return SIZE;
    }

    public void dispose() {
        itsField.removeListener(itsPropertyChangeListener);
    }
}
