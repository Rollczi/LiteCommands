plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-adventure"))

    compileOnly("net.kyori:adventure-platform-api:${Versions.ADVENTURE_PLATFORM_API}")
}

litecommandsPublish {
    artifactId = "litecommands-adventure-platform"
}