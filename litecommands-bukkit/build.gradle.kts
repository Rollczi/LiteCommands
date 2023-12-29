plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-repositories`
    `litecommands-publish`
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("io.papermc.paper:paper-mojangapi:1.20.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
}

val bukkitArtifact: String by extra

litecommandsPublish {
    artifactId = "litecommands-bukkit"
}