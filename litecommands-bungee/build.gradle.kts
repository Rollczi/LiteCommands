plugins {
    `litecommands-java`
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