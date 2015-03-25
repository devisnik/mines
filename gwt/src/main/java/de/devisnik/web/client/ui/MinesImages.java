package de.devisnik.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class MinesImages {
	private static final MinesImageBundle imageBundle = GWT
			.create(MinesImageBundle.class);

	private MinesImages() {
	}

	public static AbstractImagePrototype getFieldImagePrototype(int index) {
		switch (index) {
		case 0:
			return imageBundle.image_0();
		case 1:
			return imageBundle.image_1();
		case 2:
			return imageBundle.image_2();
		case 3:
			return imageBundle.image_3();
		case 4:
			return imageBundle.image_4();
		case 5:
			return imageBundle.image_5();
		case 6:
			return imageBundle.image_6();
		case 7:
			return imageBundle.image_7();
		case 8:
			return imageBundle.image_8();
		case 9:
			return imageBundle.image_9();
		case 10:
			return imageBundle.image_10();
		case 11:
			return imageBundle.image_11();
		case 12:
			return imageBundle.image_12();
		case 13:
			return imageBundle.image_13();
		default:
			break;
		}
		throw new IllegalArgumentException("no field image for index: " + index);
	}

	public static AbstractImagePrototype getCounterImagePrototype(int index) {
		switch (index) {
		case 0:
			return imageBundle.counter_0();
		case 1:
			return imageBundle.counter_1();
		case 2:
			return imageBundle.counter_2();
		case 3:
			return imageBundle.counter_3();
		case 4:
			return imageBundle.counter_4();
		case 5:
			return imageBundle.counter_5();
		case 6:
			return imageBundle.counter_6();
		case 7:
			return imageBundle.counter_7();
		case 8:
			return imageBundle.counter_8();
		case 9:
			return imageBundle.counter_9();
		default:
			break;
		}
		throw new IllegalArgumentException("no counter image for index: "+index);
	}
}
