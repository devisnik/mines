package de.devisnik.android.mine;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

public class MinesInfo {
	private final Context itsContext;

	public MinesInfo(Context context) {
		itsContext = context;

	}

	private PackageInfo getPackageInfo() throws NameNotFoundException {
		return itsContext.getPackageManager().getPackageInfo(itsContext.getPackageName(), 0);
	}

	public String getQueryString() {
		String versionString = "unknown";
		try {
			PackageInfo packageInfo = getPackageInfo();
			versionString = Integer.toString(packageInfo.versionCode);
		} catch (NameNotFoundException e) {
			Log.e("while reading package info", "Package name not found", e);
		}
		return "?v=" + versionString + "&av="+Build.VERSION.SDK;
	}

	public String getVersionInfo() {
		String versionString = "unknown";
		try {
			PackageInfo packageInfo = getPackageInfo();
			StringBuilder builder = new StringBuilder();
			builder.append(packageInfo.versionName);
			builder.append(" : ");
			builder.append(Integer.toString(packageInfo.versionCode));
			versionString = builder.toString();
		} catch (NameNotFoundException e) {
			Log.e("while reading package info", "Package name not found", e);
		}
		return versionString;
	}
}
