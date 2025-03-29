plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("org.telegram:telegrambots-longpolling:8.2.0")
}

litecommandsPublish {
    artifactId = "litecommands-telegrambots"
}