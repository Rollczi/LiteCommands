plugins {
    `litecommands-java-25`
    `litecommands-repositories`
    `litecommands-publish`
    id("net.fabricmc.fabric-loom") version Versions.FABRIC_LOOM_PLUGIN
}

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    // LiteCommands
    api(project(":litecommands-framework"))
    include(project(":litecommands-framework"))
    include(project(":litecommands-core"))
    include(project(":litecommands-annotations"))
    include(project(":litecommands-programmatic"))

    // Minecraft
    minecraft("com.mojang:minecraft:${Versions.FABRIC_MINECRAFT}")

    // Fabric loader and API
    implementation("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")
    implementation("net.fabricmc.fabric-api:fabric-api:${Versions.FABRIC_API}")
    implementation("net.fabricmc.fabric-api:fabric-command-api-v2:${Versions.FABRIC_COMMAND_API_V2}")
    implementation("net.fabricmc.fabric-api:fabric-lifecycle-events-v1:${Versions.FABRIC_LIFECYCLE_EVENTS_V1}")
}

litecommandsPublish {
    artifactId = "litecommands-fabric"
}