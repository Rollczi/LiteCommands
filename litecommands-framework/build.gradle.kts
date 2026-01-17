plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-core"))
    api(project(":litecommands-annotations"))
    api(project(":litecommands-programmatic"))
}

litecommandsUnit {
    useTestModuleOf(project(":litecommands-core"))
}

litecommandsPublish {
    artifactId = "litecommands-framework"
}