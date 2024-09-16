plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
    `litecommands-compile-variables`
}

dependencies {
    api("org.jetbrains:annotations:24.0.1")
}

litecommandsPublish {
    artifactId = "litecommands-core"
}