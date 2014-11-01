package de.devisnik.android.mine.input;

public interface InputDevice {

    void addListener(Listener listener);
    void removeListener(Listener listener);

    public interface Listener {
        void onLeft();
        void onRight();
        void onUp();
        void onDown();
        void onClick();
        void onDoubleClick();
    }
}