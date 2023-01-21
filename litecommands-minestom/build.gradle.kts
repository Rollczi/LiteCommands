dependencies {
    api(project(":litecommands-core"))
    compileOnly("com.github.Minestom.Minestom:Minestom:1.19.3-SNAPSHOT")
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