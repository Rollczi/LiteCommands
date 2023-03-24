plugins {
    `litecommands-java`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-core"))
    api(project(":litecommands-adventure"))

    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
}

litecommandsPublish {
    artifactId = "litecommands-velocity"
}