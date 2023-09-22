plugins {
    `litecommands-java`
    `litecommands-java-11`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))
    api(project(":litecommands-adventure"))

    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
}

litecommandsPublish {
    artifactId = "litecommands-velocity"
}
