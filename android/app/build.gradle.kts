plugins {
    id("com.android.application")
}

dependencies {
    implementation(project(":model"))
    implementation("com.google.android.instantapps:instantapps:1.1.0")
}

android {
    compileSdk = Android.COMPILE_SDK
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            signingConfig = signingConfigs.getByName("release")
        }
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false

        lintConfig = file("lint_config.xml")
    }
    namespace = "de.devisnik.android.mine"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
