plugins {
    `litecommands-java`
    `litecommands-repositories`
}

dependencies {
    api(project(":litecommands-core"))
    api("org.junit.jupiter:junit-jupiter-api:5.9.2")
    api("org.junit.jupiter:junit-jupiter-params:5.9.2")
    api("org.mockito:mockito-core:5.2.0")
    api("org.awaitility:awaitility:4.2.0")
}