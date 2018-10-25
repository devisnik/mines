import org.gradle.jvm.tasks.Jar

plugins {
    `java-library`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
}

base {
    archivesBaseName = "de.devisnik.mine.model"
}
version = "1.4.0"

dependencies {
    testImplementation("junit:junit:4.12")
}

val jarTask = tasks.named<Jar>("jar") {
    manifest {
        attributes(
                mapOf(
                        "Implementation-Title" to base.archivesBaseName,
                        "Implementation-Version" to version
                )
        )
    }
}

val sourcesJarTask by tasks.registering(Jar::class) {
    description = "Assembles a jar archive containing the sources."
    group = "build"
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

val linkJarsTask by tasks.registering(Task::class) {
    val jarProperties = "${jarTask.get().archivePath}.properties"
    inputs.files(jarTask, sourcesJarTask)
    outputs.file(jarProperties)
    doLast {
        file(jarProperties).appendText("src=${sourcesJarTask.get().archiveName}\n")
    }
}

artifacts {
    add("archives", sourcesJarTask)
    add("archives", linkJarsTask.get().outputs.files.single()) {
        type = "jar.properties" // ensure correct file extension
    }
}

tasks.named<Task>("assemble") {
    dependsOn(linkJarsTask)
}
