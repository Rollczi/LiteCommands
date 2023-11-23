plugins {
    id("litecommands.java-conventions")
}

dependencies {
    api(project(":litecommands-core"))

    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
}

val velocityArtifact: String by extra

publishing {
    publications {
        create<MavenPublication>("maven") {
            this.artifactId = velocityArtifact
            this.from(components["java"])
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}