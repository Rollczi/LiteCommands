plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-java-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
    `litecommands-compile-variables`
}

dependencies {
    api("net.jodah:expiringmap:0.5.11")
    api("org.panda-lang:expressible:1.3.6")
    api("org.jetbrains:annotations:24.0.1")
}

litecommandsPublish {
    artifactId = "litecommands-core"
}