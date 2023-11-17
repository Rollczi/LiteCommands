plugins {
    `litecommands-java`
    `litecommands-java-8`
    `litecommands-repositories`
}

dependencies {
    api(project(":litecommands-framework"))
    api("org.junit.jupiter:junit-jupiter-api:5.10.0")
    api("org.junit.jupiter:junit-jupiter-params:5.10.0")
    api("org.assertj:assertj-core:3.24.2")
    api("org.mockito:mockito-core:5.7.0")
    api("org.awaitility:awaitility:4.2.0")
}