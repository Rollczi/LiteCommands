plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-java-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-core"))
    testImplementation(project(":litecommands-core-annotations"))
    compileOnly("com.github.Minestom.Minestom:Minestom:-SNAPSHOT")
    testImplementation("com.github.Minestom.Minestom:Minestom:-SNAPSHOT")
}

litecommandsPublish {
    artifactId = "litecommands-minestom"
}