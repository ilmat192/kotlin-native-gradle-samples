import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask

plugins {
    kotlin("multiplatform") version "1.3.30-eap-125"
}

repositories {
    jcenter()
    maven { setUrl("https://dl.bintray.com/kotlin/kotlin-dev") }
}

kotlin {
    sourceSets["commonMain"].dependencies {
        implementation(kotlin("stdlib-common"))
    }

    val ios32 = iosArm32("ios32")
    val ios64 = iosArm64("ios64")
    val iosSim = iosX64("iosSim")

    // If an initial framework has a custom base name, the fat framework must have the same base name.
    // See https://youtrack.jetbrains.com/issue/KT-30805.
    val frameworkName = "my_framework"

    configure(listOf(ios32, ios64, iosSim)) {
        binaries.framework {
            baseName = frameworkName
        }
    }

    tasks.create("debugFatFramework", FatFrameworkTask::class) {
        baseName = frameworkName
        from(
            ios32.binaries.getFramework("DEBUG"),
            ios64.binaries.getFramework("DEBUG"),
            iosSim.binaries.getFramework("DEBUG")
        )
        destinationDir = buildDir.resolve("fat-framework/debug")
        group = "Universal framework"
        description = "Builds a universal (fat) debug framework"
    }


    tasks.create("releaseFatFramework", FatFrameworkTask::class) {
        baseName = frameworkName
        from(
            ios32.binaries.getFramework("RELEASE"),
            ios64.binaries.getFramework("RELEASE"),
            iosSim.binaries.getFramework("RELEASE")
        )
        destinationDir = buildDir.resolve("fat-framework/release")
        group = "Universal framework"
        description = "Builds a universal (fat) release framework"
    }
}
