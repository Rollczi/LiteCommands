plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))
    api(project(":litecommands-adventure"))
    testImplementation(project(":litecommands-annotations"))
    compileOnly("net.minestom:minestom-snapshots:${Versions.MINESTOM}")
    testImplementation("net.minestom:minestom-snapshots:${Versions.MINESTOM}")
}

litecommandsPublish {
    artifactId = "litecommands-minestom"
}