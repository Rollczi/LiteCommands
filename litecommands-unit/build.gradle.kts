plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-repositories`
}

dependencies {
    api(project(":litecommands-framework"))
    api("org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT_JUPITER}")
    api("org.junit.jupiter:junit-jupiter-params:${Versions.JUNIT_JUPITER}")
    api("org.assertj:assertj-core:${Versions.ASSERTJ}")
    api("org.mockito:mockito-core:${Versions.MOCKITO}")
    api("org.mockito:mockito-junit-jupiter:${Versions.MOCKITO}")
    api("org.awaitility:awaitility:${Versions.AWAITILITY}")
}