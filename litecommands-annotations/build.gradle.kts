plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-core"))
}

litecommandsUnit {
    useTestModuleOf(project(":litecommands-core"))
}

litecommandsPublish {
    artifactId = "litecommands-annotations"
}