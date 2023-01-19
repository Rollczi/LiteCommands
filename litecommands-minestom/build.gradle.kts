dependencies {
    api(project(":litecommands-core"))
    compileOnly("com.github.Minestom.Minestom:Minestom:c995f9c3a9")
    compileOnly("net.kyori:adventure-text-minimessage:4.12.0")
}

val minestomArtifact: String by rootProject.extra

publishing {
    publications {
        create<MavenPublication>("maven") {
            this.artifactId = minestomArtifact
            this.from(components["java"])
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}