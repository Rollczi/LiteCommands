plugins {
    `litecommands-java`
    `litecommands-java-11`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))
    api(project(":litecommands-unit"))

    implementation("com.google.code.gson:gson:${Versions.GSON}")
    implementation("com.github.ben-manes.caffeine:caffeine:${Versions.CAFFEINE}")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}

litecommandsUnit {
    useTestModuleOf(project(":litecommands-framework"))
}

litecommandsPublish {
    artifactId = "litecommands-chatgpt"
}