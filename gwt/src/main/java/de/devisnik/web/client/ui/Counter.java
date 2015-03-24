package de.devisnik.web.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Counter extends Composite {

	private int itsCount;
	private HorizontalPanel itsPanel;
	private Digit[] itsDigits;

	public Counter(int digits) {
		itsPanel = new HorizontalPanel();
		initWidget(itsPanel);
		itsDigits = new Digit[digits];
		for (int i = 0; i < itsDigits.length; i++) {
			itsDigits[i] = new Digit(0);
			itsPanel.add(itsDigits[i]);
		}
		setWidth(Integer.toString(digits*itsDigits[0].getWidth())+"px");
	}

	public void setValue(int value) {
		if (itsCount == value) {
			return;
		}
		itsCount = value;
		paintControl();
	}
	
	public int getValue() {
		return itsCount;
	}
	
	private void paintControl() {
		int displayValue = itsCount;
		for (int i = itsDigits.length-1; i >= 0; i--) {
			itsDigits[i].setValue(displayValue%10);
			displayValue /= 10;
		}
	}
}
