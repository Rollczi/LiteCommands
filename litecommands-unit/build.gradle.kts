plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-repositories`
}

dependencies {
    api(project(":litecommands-framework"))
    api(platform("org.junit:junit-bom:${Versions.JUNIT_JUPITER}"))
    api("org.junit.jupiter:junit-jupiter-api")
    api("org.junit.jupiter:junit-jupiter-params")
    api("org.assertj:assertj-core:${Versions.ASSERTJ}")
    api("org.mockito:mockito-core:${Versions.MOCKITO}")
    api("org.awaitility:awaitility:${Versions.AWAITILITY}")
}