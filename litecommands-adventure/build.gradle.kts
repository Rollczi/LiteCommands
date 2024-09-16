plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("net.kyori:adventure-text-minimessage:${Versions.ADVENTURE_MINIMESSAGES}")
    compileOnly("net.kyori:adventure-text-serializer-legacy:${Versions.ADVENTURE_TEXT_SERIALIZER_LEGACY}")
}

litecommandsPublish {
    artifactId = "litecommands-adventure"
}