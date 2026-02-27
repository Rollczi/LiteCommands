plugins {
    id("java")
    id("fabric-loom") version "1.15.4"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases/")
    maven("https://maven.fabricmc.net/")
    maven("https://api.modrinth.com/maven")
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    val yarnVersion = "1.21.11-pre5+build.1"
    val minecraftVersion = yarnVersion.substringBefore('+')
    mappings("net.fabricmc:yarn:$yarnVersion")
    minecraft("com.mojang:minecraft:$minecraftVersion")

    modImplementation("net.fabricmc:fabric-loader:0.18.4")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.143.7+26.1")

    modLocalRuntime("maven.modrinth:fabric-permissions-api:0.5.0")
    modLocalRuntime("maven.modrinth:luckperms:5.4.36-forge")
    modLocalRuntime("com.terraformersmc:modmenu:16.0.0")

    // modImplementation("dev.rollczi:litecommands-fabric:3.10.9") <-- uncomment in your project
    // modImplementation("dev.rollczi:litecommands-luckperms:3.10.9") <-- uncomment in your project
    implementation(project(path = ":litecommands-fabric", configuration = "namedElements")) // <-- REMOVE THIS
    implementation(project(":litecommands-luckperms")) // <-- REMOVE THIS
}

sourceSets.test {
    java.setSrcDirs(emptyList<String>())
    resources.setSrcDirs(emptyList<String>())
}