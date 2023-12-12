plugins {
    `litecommands-java`
    `litecommands-java-11`
    `litecommands-java-unit-test`
    `litecommands-repositories`
    `litecommands-publish`
}

dependencies {
    api(project(":litecommands-framework"))

    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("org.glassfish.expressly:expressly:5.0.0")
}

litecommandsUnit {
    useTestModuleOf(project(":litecommands-framework"))
}

litecommandsPublish {
    artifactId = "litecommands-chatgpt"
}