plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-java-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("net.dv8tion:JDA:5.0.0-beta.17")
    testImplementation(project(":litecommands-annotations"))
    testImplementation("net.dv8tion:JDA:5.0.0-beta.17")
}

litecommandsPublish {
    artifactId = "litecommands-jda"
}