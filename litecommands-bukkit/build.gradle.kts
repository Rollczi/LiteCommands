plugins {
    id("litecommands.java-conventions")
}

dependencies {
    api(project(":litecommands-core"))

    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
}

val bukkitArtifact: String by extra

publishing {
    publications {
        create<MavenPublication>("maven") {
            this.artifactId = bukkitArtifact
            this.from(components["java"])
        }
    }
}
