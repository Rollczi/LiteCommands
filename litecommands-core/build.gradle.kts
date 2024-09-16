plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
    `litecommands-compile-variables`
}

dependencies {
    api("net.jodah:expiringmap:${Versions.EXPIRING_MAP}")
    api("org.panda-lang:expressible:${Versions.EXPRESSIBLE}")
    api("org.jetbrains:annotations:${Versions.JETBRAINS_ANNOTATIONS}")
}

litecommandsPublish {
    artifactId = "litecommands-core"
}