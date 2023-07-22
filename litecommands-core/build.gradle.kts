plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-java-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api("org.panda-lang:expressible:1.3.1")
    api("org.jetbrains:annotations:24.0.1")
}

litecommandsPublish {
    artifactId = "litecommands-core"
}