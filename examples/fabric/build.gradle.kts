plugins {
    id("java")
    id("fabric-loom") version "1.8.12"
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
    val yarnVersion = "1.21.1+build.3"
    val minecraftVersion = yarnVersion.substringBefore('+')
    mappings("net.fabricmc:yarn:$yarnVersion")
    minecraft("com.mojang:minecraft:$minecraftVersion")

    modImplementation("net.fabricmc:fabric-loader:0.16.7")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.106.0+1.21.1")

    modLocalRuntime("maven.modrinth:fabric-permissions-api:0.3.3")
    modLocalRuntime("maven.modrinth:luckperms:v5.4.140-fabric")
    modLocalRuntime("com.terraformersmc:modmenu:13.0.3")

    // modImplementation("dev.rollczi:litecommands-fabric:3.10.0") <-- uncomment in your project
    // modImplementation("dev.rollczi:litecommands-luckperms:3.10.0") <-- uncomment in your project
    implementation(project(path = ":litecommands-fabric", configuration = "namedElements")) // <-- REMOVE THIS
    implementation(project(":litecommands-luckperms")) // <-- REMOVE THIS
}

sourceSets.test {
    java.setSrcDirs(emptyList<String>())
    resources.setSrcDirs(emptyList<String>())
}