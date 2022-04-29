plugins {
    id("com.android.application")
}

dependencies {
    implementation(project(":android:base"))
}

android {
    compileSdk = Android.COMPILE_SDK
    buildToolsVersion = Android.BUILD_TOOLS
    defaultConfig {
        applicationId = Android.APP_NAME
        minSdk = Android.MIN_SDK
        targetSdk = Android.TARGET_SDK
        versionCode = Android.VERSION_CODE
        versionName = Android.VERSION_NAME
    }
    signingConfigs {
        create("release") {
            if (rootProject.hasProperty("keystoreFile")) {
                storeFile = file(rootProject.property("keystoreFile")!!)
                keyAlias = rootProject.property("keystoreKeyAlias") as String
                storePassword = rootProject.property("keystoreStorePassword") as String
                keyPassword = rootProject.property("keystoreKeyPassword") as String
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
