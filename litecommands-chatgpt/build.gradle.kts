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

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}

litecommandsUnit {
    useTestModuleOf(project(":litecommands-framework"))
}

litecommandsPublish {
    artifactId = "litecommands-chatgpt"
}