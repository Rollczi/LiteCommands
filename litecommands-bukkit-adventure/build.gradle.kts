dependencies {

    api(project(":litecommands-core"))
    api(project(":litecommands-bukkit"))

    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-platform-api:4.1.2")
    compileOnly("net.kyori:adventure-text-minimessage:4.11.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.11.0")

}

val bukkitAdventureArtifact: String by rootProject.extra

publishing {
    publications {
        create<MavenPublication>("maven") {
            this.artifactId = bukkitAdventureArtifact
            this.from(components["java"])
        }
    }
}
