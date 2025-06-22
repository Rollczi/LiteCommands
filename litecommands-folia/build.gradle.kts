plugins {
    `litecommands-java`
    `litecommands-java-21`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-bukkit"))

    compileOnly("dev.folia:folia-api:${Versions.FOLIA_API}")
}

litecommandsPublish {
    artifactId = "litecommands-folia"
}