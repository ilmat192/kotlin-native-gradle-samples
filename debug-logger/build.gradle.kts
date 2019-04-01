plugins {
    id("org.jetbrains.kotlin.multiplatform").version("1.3.30-eap-125")
}

repositories {
    jcenter()
    maven {
        setUrl("https://dl.bintray.com/kotlin/kotlin-dev")
    }
}

kotlin {
    val commonMain by sourceSets
    // Configure dependencies.
    commonMain.dependencies {
        implementation(kotlin("stdlib-common"))
    }

    // Configure separate debug and release source sets.
    val commonDebug by sourceSets.creating {
        dependsOn(commonMain)
    }
    val commonRelease by sourceSets.creating {
        dependsOn(commonMain)
    }

    // Set up dependency between platform source sets and common ones:
    // If you have several targets, add dependencies for their source sets too.

    // Use the default main platform source set as a release one.
    val macosMain by  sourceSets.creating {
        dependsOn(commonRelease)
    }

    // Create another platform source set for debug sources.
    val macosDebug by sourceSets.creating {
        dependsOn(commonDebug)
    }

    // Configure a target.
    macosX64("macos") {
        // Use the default `main` compilation as a release one.
        // It uses the macosMain source set by default.
        val releaseCompilation = compilations["main"]

        // Create another compilation for the debug variant.
        // It uses the macosDebug source set by convention.
        val debugCompilation = compilations.create("debug")

        // Configure final binaries.
        binaries {
            // Configure release and debug frameworks using different sources.
            framework(listOf(DEBUG)) {
                compilation = debugCompilation
            }
            framework(listOf(RELEASE)) {
                compilation = releaseCompilation
            }

            // Configure release and debug executables using different sources.
            executable(listOf(DEBUG)) {
                entryPoint = "org.sample.logger.main"
                compilation = debugCompilation
            }
            executable(listOf(RELEASE)) {
                entryPoint = "org.sample.logger.main"
                compilation = releaseCompilation
            }
        }
    }
}

