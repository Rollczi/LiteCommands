plugins {
    `litecommands-java-17`
    `litecommands-repositories`
    `litecommands-publish`
    id("fabric-loom") version "1.5-SNAPSHOT"
}

val minecraft_version: String by project
val yarn_mappings: String by project
val fabric_loader_version: String by project
val fabric_api_version: String by project

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    // LiteCommands
    api(project(":litecommands-framework"))
    include(project(":litecommands-framework"))

    // Minecraft and mappings
    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings("net.fabricmc:yarn:${yarn_mappings}:v2")

    // Fabric loader and API
    modImplementation("net.fabricmc:fabric-loader:${fabric_loader_version}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabric_api_version}")
}

litecommandsPublish {
    artifactId = "litecommands-fabric"
    version = "$version+$minecraft_version"
}