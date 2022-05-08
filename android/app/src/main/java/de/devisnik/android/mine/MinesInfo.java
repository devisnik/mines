package de.devisnik.android.mine;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

public class MinesInfo {
    private final Context context;

    public MinesInfo(Context context) {
        this.context = context;
    }

    private PackageInfo getPackageInfo() throws NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    }

    public String getQueryString() {
        String versionString = "unknown";
        try {
            PackageInfo packageInfo = getPackageInfo();
            versionString = Integer.toString(packageInfo.versionCode);
        } catch (NameNotFoundException e) {
            Log.e("while reading package info", "Package name not found", e);
        }
        return "?v=" + versionString + "&av=" + Build.VERSION.SDK_INT;
    }

    public String getVersionInfo() {
        String versionString = "unknown";
        try {
            PackageInfo packageInfo = getPackageInfo();
            versionString = packageInfo.versionName + " : " + packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            Log.e("while reading package info", "Package name not found", e);
        }
        return versionString;
    }
}
