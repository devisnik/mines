package de.devisnik.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import de.devisnik.web.client.ui.image.MinesImageBundle;

public class MinesImages {
    private static final MinesImageBundle imageBundle = GWT.create(MinesImageBundle.class);

    private MinesImages() {
    }

    public static ImageResource getDarkFieldImageFor(int index) {
        switch (index) {
            case 0:
                return imageBundle.dark_image_0();
            case 1:
                return imageBundle.dark_image_1();
            case 2:
                return imageBundle.dark_image_2();
            case 3:
                return imageBundle.dark_image_3();
            case 4:
                return imageBundle.dark_image_4();
            case 5:
                return imageBundle.dark_image_5();
            case 6:
                return imageBundle.dark_image_6();
            case 7:
                return imageBundle.dark_image_7();
            case 8:
                return imageBundle.dark_image_8();
            case 9:
                return imageBundle.dark_image_9();
            case 10:
                return imageBundle.dark_image_10();
            case 11:
                return imageBundle.dark_image_11();
            case 12:
                return imageBundle.dark_image_12();
            case 13:
                return imageBundle.dark_image_13();
            case 14:
                return imageBundle.dark_image_14();
            case 15:
                return imageBundle.dark_image_15();
            default:
                break;
        }
        throw new IllegalArgumentException("no field image for index: " + index);
    }

    public static ImageResource getClassicFieldImageFor(int index) {
        switch (index) {
            case 0:
                return imageBundle.classic_image_0();
            case 1:
                return imageBundle.classic_image_1();
            case 2:
                return imageBundle.classic_image_2();
            case 3:
                return imageBundle.classic_image_3();
            case 4:
                return imageBundle.classic_image_4();
            case 5:
                return imageBundle.classic_image_5();
            case 6:
                return imageBundle.classic_image_6();
            case 7:
                return imageBundle.classic_image_7();
            case 8:
                return imageBundle.classic_image_8();
            case 9:
                return imageBundle.classic_image_9();
            case 10:
                return imageBundle.classic_image_10();
            case 11:
                return imageBundle.classic_image_11();
            case 12:
                return imageBundle.classic_image_12();
            case 13:
                return imageBundle.classic_image_13();
            case 14:
                return imageBundle.classic_image_14();
            case 15:
                return imageBundle.classic_image_15();
            default:
                break;
        }
        throw new IllegalArgumentException("no field image for index: " + index);
    }

    public static ImageResource getLightFieldImageFor(int index) {
        switch (index) {
            case 0:
                return imageBundle.light_image_0();
            case 1:
                return imageBundle.light_image_1();
            case 2:
                return imageBundle.light_image_2();
            case 3:
                return imageBundle.light_image_3();
            case 4:
                return imageBundle.light_image_4();
            case 5:
                return imageBundle.light_image_5();
            case 6:
                return imageBundle.light_image_6();
            case 7:
                return imageBundle.light_image_7();
            case 8:
                return imageBundle.light_image_8();
            case 9:
                return imageBundle.light_image_9();
            case 10:
                return imageBundle.light_image_10();
            case 11:
                return imageBundle.light_image_11();
            case 12:
                return imageBundle.light_image_12();
            case 13:
                return imageBundle.light_image_13();
            case 14:
                return imageBundle.light_image_14();
            case 15:
                return imageBundle.light_image_15();
            default:
                break;
        }
        throw new IllegalArgumentException("no field image for index: " + index);
    }

    public static ImageResource getCounterImageFor(int index) {
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
        throw new IllegalArgumentException("no counter image for index: " + index);
    }
}
