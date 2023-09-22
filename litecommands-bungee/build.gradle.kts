plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
}

litecommandsPublish {
    artifactId = "litecommands-bungeecord"
}