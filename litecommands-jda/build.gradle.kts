plugins {
    `litecommands-java`
    `litecommands-java-11`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("net.dv8tion:JDA:${Versions.JDA}")
    testImplementation(project(":litecommands-annotations"))
    testImplementation("net.dv8tion:JDA:${Versions.JDA}")
}

litecommandsPublish {
    artifactId = "litecommands-jda"
}