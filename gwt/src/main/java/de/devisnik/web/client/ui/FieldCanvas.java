package de.devisnik.web.client.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Image;

import de.devisnik.mine.IField;
import de.devisnik.mine.IFieldListener;

public class FieldCanvas extends Image {

	private static final Map<IField, FieldCanvas> theFieldMap = new HashMap<IField, FieldCanvas>();
	private static final IFieldListener theFieldListener = new IFieldListener() {

		public void onFieldFlagChange(final IField field, boolean value) {
			getCanvas(field).paintControl();
		}

		public void onFieldOpenChange(final IField field, boolean value) {
			getCanvas(field).paintControl();
		}

		public void onFieldExplodedChange(IField field, boolean value) {
			getCanvas(field).paintControl();
		}
		
		public void onFieldTouchedChange(IField field, boolean value) {
			getCanvas(field).paintControl();
		};
	};
	private final IField itsField;
	private final int itsPositionX;
	private final int itsPositionY;

	public FieldCanvas(final IField field, int positionX, int positionY) {
		super();
		this.itsField = field;
		this.itsPositionX = positionX;
		this.itsPositionY = positionY;
		paintControl();
		addCanvas(itsField, this);
		itsField.addListener(theFieldListener);
	}

	private static void addCanvas(IField field, FieldCanvas canvas) {
		theFieldMap.put(field, canvas);
	}

	private static FieldCanvas getCanvas(IField field) {
		return theFieldMap.get(field);
	}

	private void paintControl() {
		int imgnr = 0;
		if (itsField.isOpen()) {
			if (itsField.isBomb()) {
				if (itsField.isExploded()) {
					imgnr = 13;
				} else {
					imgnr = 9;
				}
			} else {
				if (itsField.isFlagged()) {
					imgnr = 12;
				} else {
					imgnr = itsField.getNeighborBombs();
				}
			}
		} else {
			if (itsField.isFlagged()) {
				imgnr = 11;
			} else {
				imgnr = 10;
			}
		}
		MinesImages.getFieldImagePrototype(imgnr).applyTo(this);
	}

	public void dispose() {
		itsField.removeListener(theFieldListener);
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
