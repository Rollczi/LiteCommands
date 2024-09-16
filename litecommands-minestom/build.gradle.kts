plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))
    api(project(":litecommands-adventure"))
    testImplementation(project(":litecommands-annotations"))
    compileOnly("dev.hollowcube:minestom-ce:${Versions.MINESTOM_CE}")
    testImplementation("dev.hollowcube:minestom-ce:${Versions.MINESTOM_CE}")
}

litecommandsPublish {
    artifactId = "litecommands-minestom"
}