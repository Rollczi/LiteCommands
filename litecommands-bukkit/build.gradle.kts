plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")
    compileOnly("org.spigotmc:spigot:${Versions.SPIGOT}")
    compileOnly("com.comphenix.protocol:ProtocolLib:${Versions.PROTOCOL_LIB}")
}

val bukkitArtifact: String by extra

litecommandsPublish {
    artifactId = "litecommands-bukkit"
}