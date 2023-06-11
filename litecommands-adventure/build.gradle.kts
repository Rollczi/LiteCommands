plugins {
    `litecommands-java`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-core"))

    compileOnly("net.kyori:adventure-text-minimessage:4.13.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.13.0")
}

litecommandsPublish {
    artifactId = "litecommands-adventure"
}