plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-java-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
    `litecommands-java-benchmark`
}

dependencies {
    api(project(":litecommands-core"))
    jmhImplementation(project(":litecommands-unit"))
}

litecommandsUnit {
    useTestModuleOf(project(":litecommands-core"))
}

litecommandsPublish {
    artifactId = "litecommands-core-annotations"
}