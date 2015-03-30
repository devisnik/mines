package de.devisnik.web.client.ui;

import com.google.gwt.user.client.ui.Image;

public class Digit extends Image {

	private int itsValue;

	public Digit(int value) {
		setValue(value);
		paintControl();
	}

	public void setValue(int value) {
		if (value < 0 || value > 9) {
			throw new IllegalArgumentException("no valid digit: " + value);
		}
		if (itsValue == value) {
			return;
		}
		itsValue = value;
		paintControl();
	}

	private void paintControl() {
		setResource(MinesImages.getCounterImageFor(itsValue));
	}
}
