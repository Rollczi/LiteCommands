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
    compileOnly("com.github.Minestom:Minestom:2cdb3911")
    testImplementation("com.github.Minestom:Minestom:2cdb3911")
}

litecommandsPublish {
    artifactId = "litecommands-minestom"
}