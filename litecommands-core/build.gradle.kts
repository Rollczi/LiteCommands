plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
    `litecommands-compile-variables`
}

dependencies {
    api("org.jetbrains:annotations:${Versions.JETBRAINS_ANNOTATIONS}")
}

litecommandsPublish {
    artifactId = "litecommands-core"
}