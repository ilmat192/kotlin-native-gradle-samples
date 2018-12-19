plugins {
    id("org.jetbrains.kotlin.multiplatform").version("1.3.20-eap-52")
}

kotlin {
    sourceSets.create("macosMain").apply {
        dependencies {
            // Note: the exported dependencies must be added in the API configuration.
            api(project(":exported"))
        }
    }

    macosX64("macos") {
        binaries {
            // Declare a framework based on the main compilation and
            // built in two variants: debug and release.
            framework(listOf(DEBUG, RELEASE)) {
                // Add a dependency to be exported in the framework.
                // Note that by default exporting is not transitive.
                // You can enable the transitive mode by changing
                // the corresponding setting:
                // transitiveExport = true
                export(project(":exported"))

                // It's possible to declare a maven dependency to be exported.
                // But due to current limitations of Gradle metadata such a dependency
                // should be either a platform one (e.g.
                // 'kotlinx-coroutines-core-native_debug_macos_x64' instead of
                // 'kotlinx-coroutines-core-native') or be exported transitively.
            }
        }
    }
}

