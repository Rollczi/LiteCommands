plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("net.md-5:bungeecord-api:${Versions.BUNGEECORD_API}")
}

litecommandsPublish {
    artifactId = "litecommands-bungeecord"
}