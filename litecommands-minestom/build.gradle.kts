plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-java-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))
    testImplementation(project(":litecommands-annotations"))
    compileOnly("dev.hollowcube:minestom-ce:438338381e")
    testImplementation("dev.hollowcube:minestom-ce:438338381e")
}

litecommandsPublish {
    artifactId = "litecommands-minestom"
}