import org.jetbrains.kotlin.gradle.utils.extendsFrom

plugins {
    id("java")
    id("net.fabricmc.fabric-loom") version "1.17.11"
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases/")
    maven("https://maven.fabricmc.net/")
    maven("https://api.modrinth.com/maven")
    maven("https://maven.terraformersmc.com/releases/")
}


configurations {
    localRuntime
    runtimeClasspath.extendsFrom(localRuntime)
    testRuntimeOnly.extendsFrom(localRuntime)
}

dependencies {
    minecraft("com.mojang:minecraft:26.2")

    implementation("net.fabricmc:fabric-loader:0.19.3")
    implementation("net.fabricmc.fabric-api:fabric-api:0.152.1+26.2")

    localRuntime("maven.modrinth:fabric-permissions-api:0.7.0")
//    localRuntime("maven.modrinth:luckperms:5.4.36-fabric") TODO we need wait for release
//    localRuntime("net.fabricmc.fabric-api:fabric-command-api-v1:1.2.56+f71b366f73")
//    localRuntime("net.fabricmc.fabric-api:fabric-networking-v0:0.1.0+f1618918")
//    localRuntime("com.terraformersmc:modmenu:18.0.0") TODO: bump to 19 when relased https://www.curseforge.com/minecraft/mc-mods/modmenu/files/all?page=1&pageSize=20&showAlphaFiles=hide


    // implementation("dev.rollczi:litecommands-fabric:3.10.9") <-- uncomment in your project
    // implementation("dev.rollczi:litecommands-luckperms:3.10.9") <-- uncomment in your project
    implementation(project(":litecommands-fabric")) // <-- REMOVE THIS
//    implementation(project(":litecommands-luckperms")) // <-- REMOVE THIS // TODO uncomment this
}

sourceSets.test {
    java.setSrcDirs(emptyList<String>())
    resources.setSrcDirs(emptyList<String>())
}