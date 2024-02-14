plugins {
    `litecommands-java-17`
    `litecommands-repositories`
    `litecommands-publish`
    id("fabric-loom") version "1.5-SNAPSHOT"
}

val minecraft_version: String by project
val yarn_mappings: String by project
val loader_version: String by project
val fabric_version: String by project

dependencies {
    include(project(":litecommands-framework"))
    modApi("dev.rollczi:litecommands-framework:3.3.4")

    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings("net.fabricmc:yarn:${yarn_mappings}:v2")
    modImplementation("net.fabricmc:fabric-loader:${loader_version}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabric_version}")
}

litecommandsPublish {
    artifactId = "litecommands-fabric"
    version = "$version+$minecraft_version"
}