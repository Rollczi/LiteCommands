plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("org.telegram:telegrambots-meta:${Versions.TELEGRAM_BOTS}")
}

litecommandsPublish {
    artifactId = "litecommands-telegrambots"
}