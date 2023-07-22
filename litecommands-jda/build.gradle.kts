plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-java-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-core"))

    compileOnly("net.dv8tion:JDA:5.0.0-beta.6")
    testImplementation(project(":litecommands-core-annotations"))
    testImplementation("net.dv8tion:JDA:5.0.0-beta.6")
}

litecommandsPublish {
    artifactId = "litecommands-jda"
}