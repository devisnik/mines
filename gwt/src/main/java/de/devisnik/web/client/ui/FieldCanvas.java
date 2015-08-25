package de.devisnik.web.client.ui;

import com.google.gwt.user.client.ui.Image;
import de.devisnik.mine.IField;
import de.devisnik.mine.SimpleFieldListener;

public class FieldCanvas extends Image {

    public static final int SIZE = 25;

    public static final class CanvasUpdatingFieldListener extends SimpleFieldListener {

        private final FieldCanvas canvas;

        public CanvasUpdatingFieldListener(FieldCanvas canvas) {
            this.canvas = canvas;
        }

        @Override
        protected void onChange(IField field) {
            canvas.paintControl();
        }
    }

    private final IField itsField;
    private final int itsPositionX;
    private final int itsPositionY;
    private final CanvasUpdatingFieldListener fieldListener;

    public FieldCanvas(final IField field, int positionX, int positionY) {
        super();
        this.itsField = field;
        this.itsPositionX = positionX;
        this.itsPositionY = positionY;
        setSize(SIZE + "px", SIZE + "px");
        paintControl();
        fieldListener = new CanvasUpdatingFieldListener(this);
        itsField.addListener(fieldListener);
    }

    private void paintControl() {
        setResource(MinesImages.getDarkFieldImageFor(itsField.getImage()));
    }

    public void dispose() {
        itsField.removeListener(fieldListener);
    }

    public IField getField() {
        return itsField;
    }

    public int getX() {
        return itsPositionX;
    }

    public int getY() {
        return itsPositionY;
    }
}
