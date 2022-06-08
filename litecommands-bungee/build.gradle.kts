dependencies {
    api(project(":litecommands-core"))

    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
}

val bungeeArtifact: String by rootProject.extra

publishing {
    publications {
        create<MavenPublication>("maven") {
            this.artifactId = bungeeArtifact
            this.from(components["java"])
        }
    }
}
