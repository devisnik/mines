buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.3.1")
        classpath("com.novoda:gradle-android-command-plugin:2.0.1")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.20.0")
        classpath("com.github.triplet.gradle:play-publisher:1.2.2")
        classpath("com.getkeepsafe.dexcount:dexcount-gradle-plugin:0.8.4")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
    apply(plugin = "com.github.ben-manes.versions")
    apply(plugin = "idea")
}
