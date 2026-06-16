plugins {
    `litecommands-java`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-core"))

    compileOnly("net.luckperms:api:${Versions.LUCKPERMS_API}")
}

litecommandsPublish {
    artifactId = "litecommands-luckperms"
}