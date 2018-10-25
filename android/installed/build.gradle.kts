plugins {
    id("com.android.application")
    id("android-command")
    id("com.github.triplet.play")
    id("com.getkeepsafe.dexcount")
}

dependencies {
    implementation(project(":android:base"))
}

android {
    compileSdkVersion(Android.COMPILE_SDK)
    buildToolsVersion(Android.BUILD_TOOLS)
    defaultConfig {
        applicationId = Android.APP_NAME
        minSdkVersion(Android.MIN_SDK)
        targetSdkVersion(Android.TARGET_SDK)
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

if (rootProject.hasProperty("playstoreServiceAccount")) {
    play {
        serviceAccountEmail = rootProject.property("playstoreServiceAccount") as String
        pk12File = rootProject.file(rootProject.property("playstorePk12File") as String)
        setTrack("production")
    }
}

//task archiveRelease(type: Copy) {
//    from "build/outputs/apk", "build/outputs/"
//    into "releases/${Android.VERSION_CODE}"
//    include("android-release.apk", "mapping/**")
//    rename("android-release.apk", "${Android.APP_NAME}_${new Date().format("yyyyMMdd")}_${Android.VERSION_NAME}_${Android.VERSION_CODE}.apk")
//}
