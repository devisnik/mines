package de.devisnik.android.mine;

import android.util.Log;

public class Logger {

    private static final boolean ENABLED = false;
    private final String tag;

    public Logger(Class<?> clazz) {
        tag = clazz.getSimpleName();
    }

    public void d(String msg) {
        if (ENABLED)
            Log.d(tag, msg);
    }

    public void d(String msg, Throwable tr) {
        if (ENABLED)
            Log.d(tag, msg, tr);
    }

    public void i(String msg) {
        if (ENABLED)
            Log.i(tag, msg);
    }

    public void i(String msg, Throwable tr) {
        if (ENABLED)
            Log.i(tag, msg, tr);
    }

    public void w(String msg) {
        if (ENABLED)
            Log.w(tag, msg);
    }

    public void w(String msg, Throwable tr) {
        if (ENABLED)
            Log.w(tag, msg, tr);
    }

    public void e(String msg) {
        if (ENABLED)
            Log.e(tag, msg);
    }

    public void e(String msg, Throwable tr) {
        if (ENABLED)
            Log.e(tag, msg, tr);
    }
}
