dependencies {
    api(project(":litecommands-core"))
    api(project(":shared:adventure"))

    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
}

val velocityArtifact: String by rootProject.extra

publishing {
    publications {
        create<MavenPublication>("maven") {
            this.artifactId = velocityArtifact
            this.from(components["java"])
        }
    }
}
