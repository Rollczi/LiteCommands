plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-java-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-core"))
    api(project(":litecommands-annotation"))
    api(project(":litecommands-programmatic"))
}

litecommandsUnit {
    useTestModuleOf(project(":litecommands-core"))
}

litecommandsPublish {
    artifactId = "litecommands-programmatic"
}