plugins {
    `litecommands-java`
    `litecommands-java-21`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))
    api(project(":litecommands-adventure"))
    testImplementation(project(":litecommands-annotations"))
    compileOnly("net.minestom:minestom-snapshots:4305006e6b")
    testImplementation("net.minestom:minestom-snapshots:4305006e6b")
}

litecommandsPublish {
    artifactId = "litecommands-minestom"
}