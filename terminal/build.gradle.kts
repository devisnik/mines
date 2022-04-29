plugins {
    groovy
    application
}

application {
    mainClassName = "Shell"
}

dependencies {
    implementation(project(":model"))
    implementation(project(":robot"))
    implementation(localGroovy())
    implementation("commons-cli:commons-cli:1.3.1")
    implementation("jline:jline:2.14.6")
}
