plugins {
    `litecommands-java-17`
    `litecommands-java-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-adventure"))
    api(project(":litecommands-bukkit"))

    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
}

litecommandsUnit {
    useTestModuleOf(project(":litecommands-core"))
    useTestModuleOf(project(":litecommands-adventure"))
    useTestModuleOf(project(":litecommands-bukkit"))

    use("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
}

litecommandsPublish {
    artifactId = "litecommands-paper"
}

