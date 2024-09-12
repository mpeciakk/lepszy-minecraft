plugins {
    kotlin("jvm") version "1.9.20"
    application
}

group = "mpeciakk"
version = "1.0-SNAPSHOT"

val lwjglVersion = "3.3.3"
val jomlVersion = "1.10.5"

val lwjglNatives = Pair(
    System.getProperty("os.name")!!,
    System.getProperty("os.arch")!!
).let { (name, _) ->
    when {
        arrayOf("Linux", "FreeBSD", "SunOS", "Unit").any { name.startsWith(it) } ->
            "natives-linux"

        arrayOf("Windows").any { name.startsWith(it) } ->
            "natives-windows"

        arrayOf("Mac").any { name.startsWith(it) } ->
            "natives-macos-arm64"

        else -> throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

application {
    mainClass.set("MainKt")
}

tasks {
    processResources {
        from("src/main/resources")
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    implementation("org.joml", "joml", jomlVersion)
}

kotlin {
    jvmToolchain(8)
}

