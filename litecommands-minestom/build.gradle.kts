plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-java-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))
    api(project(":litecommands-adventure"))
    testImplementation(project(":litecommands-annotations"))
    compileOnly("dev.hollowcube:minestom-ce:5347c0b11f")
    testImplementation("dev.hollowcube:minestom-ce:5347c0b11f")
}

litecommandsPublish {
    artifactId = "litecommands-minestom"
}