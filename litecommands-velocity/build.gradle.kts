plugins {
    `litecommands-java-17`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-core"))
    api(project(":litecommands-adventure"))

    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
}

litecommandsPublish {
    artifactId = "litecommands-velocity"
}