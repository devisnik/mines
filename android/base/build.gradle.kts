plugins {
    id("com.android.feature")
}

dependencies {
    implementation(project(":model"))
    implementation("com.google.android.instantapps:instantapps:1.1.0")
}

android {
    baseFeature = true
    compileSdkVersion(Android.COMPILE_SDK)
    buildToolsVersion(Android.BUILD_TOOLS)

    defaultConfig {
        minSdkVersion(Android.MIN_SDK)
        targetSdkVersion(Android.TARGET_SDK)
        versionCode = Android.VERSION_CODE
        versionName = Android.VERSION_NAME
    }

    lintOptions {
        isCheckReleaseBuilds = false
        isAbortOnError = false

        lintConfig = file("lint_config.xml")
    }
}
