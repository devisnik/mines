import java.util.Locale.getDefault

plugins {
    application
}

val swt = Analyzer().runtime()!!
val version = "3.128.0"
val swtArtifactName = "org.eclipse.swt.${swt.windowing}.${swt.platform}.${swt.architecture}:$version"

dependencies {
    implementation(project(":model"))
    implementation(project(":robot"))
    implementation("org.eclipse.platform:${swtArtifactName}")
}

application {
    mainClass = "de.devisnik.mine.swt.MinesScreen"
    if (swt.platform == "macosx") {
        applicationDefaultJvmArgs = listOf("-XstartOnFirstThread")
    }
}

configurations.configureEach {
    resolutionStrategy {
        dependencySubstitution {
            substitute(module("org.eclipse.platform:org.eclipse.swt.\${osgi.platform}")).using(
                module("org.eclipse.platform:${swtArtifactName}")
            )
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

class SWT(
    val windowing: String,
    val platform: String,
    val architecture: String,
)

class Analyzer {

    private operator fun Regex.contains(text: CharSequence): Boolean = this.containsMatchIn(text)

    fun runtime(): SWT? {
        val platform = System.getProperty("os.name")
        val architecture = System.getProperty("os.arch")
        val windowingLibrary = windowingLibrary(platform) ?: return null
        val swtPlatform = platform(platform)
        val swtArch = arch(architecture)
        return SWT(windowingLibrary, swtPlatform, swtArch)
    }

    fun windowingLibrary(platform: String): String? {
        return when (platform.replace(" ", "").lowercase(getDefault())) {
            in Regex(".*linux.*") -> "gtk"
            in Regex(".*darwin.*") -> "cocoa"
            in Regex(".*osx.*") -> "cocoa"
            in Regex(".*win.*") -> "win32"
            else -> null
        }
    }

    fun arch(arch: String): String {
        return when (arch) {
            "aarch64" -> arch
            in Regex(".*64.*") -> "x86_64"
            else -> "x86"
        }
    }

    fun platform(platform: String): String {
        return when (platform.replace(" ", "").lowercase(getDefault())) {
            in Regex(".*linux.*") -> "linux"
            in Regex(".*darwin.*") -> "macosx"
            in Regex(".*osx.*") -> "macosx"
            in Regex(".*win.*") -> "win32"
            else -> platform
        }
    }
}
