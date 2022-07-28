dependencies {
    api(project(":litecommands-core"))

    compileOnly("org.spigotmc:spigot-api:1.19.1-R0.1-SNAPSHOT")
}

val bukkitArtifact: String by rootProject.extra

publishing {
    publications {
        create<MavenPublication>("maven") {
            this.artifactId = bukkitArtifact
            this.from(components["java"])
        }
    }
}
