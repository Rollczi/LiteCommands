plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("net.md-5:bungeecord-api:1.20-R0.1")
}

litecommandsPublish {
    artifactId = "litecommands-bungeecord"
}