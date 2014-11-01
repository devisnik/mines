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

    public enum Event {
        LEFT(1),
        RIGHT(2),
        UP(3),
        DOWN(4),
        CLICK(5),
        DOUBLE_CLICK(6),
        UNKNOWN(-1);

        public final byte code;

        Event(int code){
            this.code = (byte) code;
        }

        public byte getCode() {
            return code;
        }

        public static Event from(int eventCode) {
            for (Event e: values()){
                if (eventCode == e.getCode()){
                    return e;
                }
            }
            return Event.UNKNOWN;
        }
    }
}