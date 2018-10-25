mapOf(
        "VERSION_CODE" to 63,
        "VERSION_NAME" to "1.11.2",
        "APP_NAME" to "de.devisnik.android.mine",
        "MIN_SDK" to 21,
        "TARGET_SDK" to 27,
        "COMPILE_SDK" to 28,
        "BUILD_TOOLS" to "28.0.3"
)
        .forEach { e ->
            rootProject.extra.set(e.key, e.value)
        }