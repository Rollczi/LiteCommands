plugins {
    `litecommands-java-17`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-core"))

    compileOnly("net.dv8tion:JDA:5.0.0-beta.6")
}

litecommandsPublish {
    artifactId = "litecommands-jda"
}