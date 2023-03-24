plugins {
    `litecommands-java`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-core"))

    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
}

litecommandsPublish {
    artifactId = "litecommands-bungeecord"
}