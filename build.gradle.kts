buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.42.0")
        classpath("org.wisepersist:gwt-gradle-plugin:1.1.18")

    }
}

//plugins {
//  id("com.gradle.build-scan") version "2.4"
//}
//
//buildScan {
//    setTermsOfServiceUrl("https://gradle.com/terms-of-service")
//    setTermsOfServiceAgree("yes")
//    publishAlways()
//    isCaptureTaskInputFiles = true
//}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
    apply(plugin = "com.github.ben-manes.versions")
}
