plugins {
    `litecommands-java`
    `litecommands-java-17`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")
    compileOnly("net.dmulloy2:ProtocolLib:${Versions.PROTOCOL_LIB}")
    testImplementation("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")
    testImplementation("org.mockito:mockito-core:${Versions.MOCKITO}")
}

litecommandsPublish {
    artifactId = "litecommands-bukkit"
}

tasks.compileTestJava {
    javaCompiler = javaToolchains.compilerFor {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.test {
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(11))
    })
}