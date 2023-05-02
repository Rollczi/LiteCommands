plugins {
    `litecommands-java-17`
    `litecommands-java-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-core"))
    compileOnly("com.github.Minestom.Minestom:Minestom:-SNAPSHOT")
}

litecommandsPublish {
    artifactId = "litecommands-minestom"
}