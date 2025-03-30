plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")
    compileOnly("com.comphenix.protocol:ProtocolLib:${Versions.PROTOCOL_LIB}")
    testImplementation("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")
}

litecommandsPublish {
    artifactId = "litecommands-bukkit"
}