plugins {
    id("java-library")
    id("maven-publish")
}

group = "dev.rollczi.litecommands"
version = "2.8.6"

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    repositories {
        mavenLocal()

        maven(
            "panda",
            "https://repo.panda-lang.org",
            "MAVEN_USERNAME",
            "MAVEN_PASSWORD",
            false
        )

        maven(
            "eternalcode",
            "https://repo.eternalcode.pl",
            "ETERNAL_CODE_MAVEN_USERNAME",
            "ETERNAL_CODE_MAVEN_PASSWORD"
        )

        maven(
            "minecodes",
            "https://repository.minecodes.pl",
            "MINE_CODES_MAVEN_USERNAME",
            "MINE_CODES_MAVEN_PASSWORD",
        )
    }
}

fun RepositoryHandler.maven(
    name: String,
    url: String,
    username: String,
    password: String,
    deploySnapshots: Boolean = true
) {
    val isSnapshot = version.toString().endsWith("-SNAPSHOT")

    if (isSnapshot && !deploySnapshots) {
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