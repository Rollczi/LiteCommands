plugins {
    id("java")
    id("fabric-loom") version "1.7-SNAPSHOT"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases/")
    maven("https://maven.fabricmc.net/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.20.4")
    mappings("net.fabricmc:yarn:1.20.4+build.3:v2")

    modImplementation("net.fabricmc:fabric-loader:0.15.6")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.96.1+1.20.4")

//    modImplementation("dev.rollczi:litecommands-fabric:3.4.1") // <-- uncomment in your project
    implementation(project(path = ":litecommands-fabric", configuration = "namedElements"))

}

sourceSets.test {
    java.setSrcDirs(emptyList<String>())
    resources.setSrcDirs(emptyList<String>())
}
