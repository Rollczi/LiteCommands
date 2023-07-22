plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-adventure"))
    api(project(":litecommands-bukkit"))

    compileOnly("net.kyori:adventure-platform-api:4.3.0")
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
}

litecommandsPublish {
    artifactId = "litecommands-bukkit-adventure-platform"
}