plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))
    api(project(":litecommands-adventure"))

    compileOnly("com.velocitypowered:velocity-api:${Versions.VELOCITY_API}")
}

litecommandsPublish {
    artifactId = "litecommands-velocity"
}
