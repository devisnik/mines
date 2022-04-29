plugins {
    id("com.android.library")
}

dependencies {
    implementation(project(":model"))
    implementation("com.google.android.instantapps:instantapps:1.1.0")
}

android {
    compileSdk = Android.COMPILE_SDK
    buildToolsVersion = Android.BUILD_TOOLS

    defaultConfig {
        minSdk = Android.MIN_SDK
        targetSdk = Android.TARGET_SDK
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false

        lintConfig = file("lint_config.xml")
    }
}
