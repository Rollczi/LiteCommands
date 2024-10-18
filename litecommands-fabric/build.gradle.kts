plugins {
    `litecommands-java-17`
    `litecommands-repositories`
    `litecommands-publish`
    id("fabric-loom") version "1.8.10"
}

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    // LiteCommands
    api(project(":litecommands-framework"))
    include(project(":litecommands-framework"))

    // Minecraft and mappings
    minecraft("com.mojang:minecraft:${Versions.FABRIC_MINECRAFT}")
    mappings("net.fabricmc:yarn:${Versions.FABRIC_YARN_MAPPINGS}:v2")

    // Fabric loader and API
    modImplementation("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.FABRIC_API}")
}

litecommandsPublish {
    artifactId = "litecommands-fabric"
}