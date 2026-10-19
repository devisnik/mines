buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:9.4.1")
    }
}

plugins {
    id("io.github.ben-manes.versions") version "0.66.0"
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
