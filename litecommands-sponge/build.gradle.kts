plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))
    compileOnly("org.spongepowered:spongeapi:8.2.0")

}

val bukkitArtifact: String by extra

litecommandsPublish {
    artifactId = "litecommands-sponge"
}