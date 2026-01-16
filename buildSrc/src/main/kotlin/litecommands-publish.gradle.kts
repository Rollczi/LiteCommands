plugins {
    id("java-library")
    id("maven-publish")
}

group = "dev.rollczi"
version = "3.10.10-SNAPSHOT"

publishing {
    java {
        withSourcesJar()
        withJavadocJar()
    }

    repositories {
        mavenLocal()

        maven(
            name = "panda",
            url = "https://repo.panda-lang.org",
            username = "MAVEN_USERNAME",
            password = "MAVEN_PASSWORD",
            snapshots = false,
            beta = false,
        )

        maven(
            name = "eternalcode",
            url = "https://repo.eternalcode.pl",
            username = "ETERNAL_CODE_MAVEN_USERNAME",
            password = "ETERNAL_CODE_MAVEN_PASSWORD",
            snapshots = true,
            beta = true,
        )
    }
}

fun RepositoryHandler.maven(
    name: String,
    url: String,
    username: String,
    password: String,
    snapshots: Boolean = true,
    beta: Boolean = false
) {
    val isSnapshot = version.toString().endsWith("-SNAPSHOT")

    if (isSnapshot && !snapshots) {
        return
    }

    val isBeta = version.toString().contains("-BETA")

    if (isBeta && !beta) {
        return
    }

    this.maven {
        this.name =
            if (isSnapshot) "${name}Snapshots"
            else "${name}Releases"

        this.url =
            if (isSnapshot) uri("$url/snapshots")
            else uri("$url/releases")

        this.credentials {
            this.username = System.getenv(username)
            this.password = System.getenv(password)
        }
    }
}

interface LitePublishExtension {
    var artifactId: String
}

val extension = extensions.create<LitePublishExtension>("litecommandsPublish")

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = extension.artifactId
                from(project.components["java"])
            }
        }
    }
}