plugins {
    `litecommands-java`
    `litecommands-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    implementation("com.google.code.gson:gson:${Versions.GSON}")
    implementation("com.github.ben-manes.caffeine:caffeine:${Versions.CAFFEINE}")
    implementation("com.squareup.okhttp3:okhttp:${Versions.OKHTTP}")
}

litecommandsUnit {
    useTestModuleOf(project(":litecommands-framework"))
}

litecommandsPublish {
    artifactId = "litecommands-chatgpt"
}