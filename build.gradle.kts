/*
 * Copyright (c) 2024 Dmitry Osin <d@osin.pro>
 */

plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "pro.osin.tools"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.telegram:telegrambots:6.8.0")
    implementation("com.github.twitch4j:twitch4j:1.23.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("net.dv8tion:JDA:5.0.0-beta.18")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "BootstrapperKt"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}