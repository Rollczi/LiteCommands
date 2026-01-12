plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("org.telegram:telegrambots-meta:9.2.1")
}

litecommandsPublish {
    artifactId = "litecommands-telegrambots"
}