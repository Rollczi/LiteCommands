plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    compileOnly("net.kyori:adventure-text-minimessage:${Versions.ADVENTURE_MINIMESSAGES}")
    compileOnly("net.kyori:adventure-text-serializer-legacy:${Versions.ADVENTURE_TEXT_SERIALIZER_LEGACY}")
    testImplementation("net.kyori:adventure-text-minimessage:${Versions.ADVENTURE_MINIMESSAGES}")
    testImplementation("net.kyori:adventure-text-serializer-legacy:${Versions.ADVENTURE_TEXT_SERIALIZER_LEGACY}")
}

litecommandsPublish {
    artifactId = "litecommands-adventure"
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