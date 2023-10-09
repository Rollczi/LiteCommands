plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-adventure"))

    compileOnly("net.kyori:adventure-platform-api:4.3.1")
}

litecommandsPublish {
    artifactId = "litecommands-adventure-platform"
}