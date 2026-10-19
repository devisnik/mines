plugins {
    groovy
    application
}

application {
    mainClass = "Shell"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

dependencies {
    implementation(project(":model"))
    implementation(project(":robot"))
    implementation("org.codehaus.groovy:groovy-all:3.0.9")
    implementation("commons-cli:commons-cli:1.3.1")
    implementation("jline:jline:2.14.6")
}
