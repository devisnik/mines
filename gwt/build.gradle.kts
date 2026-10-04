plugins {
    id("org.docstr.gwt") version "2.2.9"
}

// needed to avoid crash in gwt-closure below due to project not resolved
evaluationDependsOn(":robot")
evaluationDependsOn(":model")

dependencies {
    implementation(project(":robot"))
    implementation(project(":model"))
}

gwt {
//    gwtVersion='2.8.2'
//    minHeapSize = '512M'
//    maxHeapSize = '4096M'

    modules = listOf("de.devisnik.web.Mines")
    extraSourceDirs = files(
        files(project(":model").the<SourceSetContainer>()["main"].allJava.srcDirs).asPath,
        files(project(":model").the<SourceSetContainer>()["main"].output.resourcesDir).asPath,
        files(project(":robot").the<SourceSetContainer>()["main"].allJava.srcDirs).asPath,
        files(project(":robot").the<SourceSetContainer>()["main"].output.resourcesDir).asPath,
    )

    devMode {
        startupUrl = "de.devisnik.web.Mines/Mines.html"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
