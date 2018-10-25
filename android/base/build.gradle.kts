plugins {
    id("com.android.feature")
}

dependencies {
    implementation(project(":model"))
    implementation("com.google.android.instantapps:instantapps:1.1.0")
}

android {
    baseFeature = true
    compileSdkVersion(rootProject.extra["COMPILE_SDK"] as Int)
    buildToolsVersion(rootProject.extra["BUILD_TOOLS"] as String)
    defaultConfig {
        minSdkVersion(rootProject.extra["MIN_SDK"] as Int)
        targetSdkVersion(rootProject.extra["TARGET_SDK"] as Int)
        versionCode = rootProject.extra["VERSION_CODE"] as Int
        versionName = rootProject.extra["VERSION_NAME"] as String
    }

    lintOptions {
        isCheckReleaseBuilds = false
        isAbortOnError = false

        lintConfig = file("lint_config.xml")
    }
}
