package de.devisnik.mine.swt;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import java.io.IOException;
import java.io.InputStream;

public final class MinesImages {

    private Image[] itsFieldImages;
    private Image[] itsCounterImages;


    private Display display;

    MinesImages(Display display) {
        this.display = display;
    }

    public Image[] getFieldImages() {
        if (itsFieldImages == null) {
            itsFieldImages = readFieldImages();
        }
        return itsFieldImages;
    }

    public Image[] getCounterImages() {
        if (itsCounterImages == null) {
            itsCounterImages = readCounterImages();
        }
        return itsCounterImages;
    }

    private Image[] readFieldImages() {
        Image[] images = new Image[16];
        for (int i = 0; i < images.length; i++) {
            String imageNumber = String.valueOf(i);
            if (imageNumber.length() == 1)
                imageNumber = "0" + imageNumber;

            images[i] = createImage("/image/classic_image_" + imageNumber + ".png");
        }
        return images;
    }

    private Image createImage(String path) {
        InputStream stream = MinesImages.class.getResourceAsStream(path);
        try {
            if (stream != null)
                return new Image(display, stream);
            else
                return new Image(display, "./src/main/resources" + path);
        }
        finally {
            if (stream != null)
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private Image[] readCounterImages() {
        Image[] images = new Image[10];
        for (int i = 0; i < images.length; i++) {
            String imageNumber = String.valueOf(i);
            images[i] = createImage("/image/counter_" + imageNumber + ".gif");
        }
        return images;
    }
}
