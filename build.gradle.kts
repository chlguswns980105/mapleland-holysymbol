import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.hyeonjunchoi.timer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.github.kwhat:jnativehook:2.2.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("") // `-all` 붙지 않게
    mergeServiceFiles()       // JNativeHook 같은 서비스 리소스 충돌 방지
}
