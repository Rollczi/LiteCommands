rootProject.name = "LiteCommands"

// core
include(":litecommands-core")
include(":litecommands-annotations")
include(":litecommands-programmatic")
include(":litecommands-framework")
include(":litecommands-unit")

// extensions
include(":litecommands-adventure")
include(":litecommands-adventure-platform")
include(":litecommands-chatgpt", JavaVersion.VERSION_11)
include(":litecommands-jakarta", JavaVersion.VERSION_11)

// platforms
include(":litecommands-velocity", JavaVersion.VERSION_11)
include(":litecommands-bungee")
include(":litecommands-bukkit")
include(":litecommands-minestom", JavaVersion.VERSION_17)
include("litecommands-jda", JavaVersion.VERSION_17)
include(":litecommands-sponge")
include(":litecommands-fabric", JavaVersion.VERSION_17)

// examples
include(":examples:bukkit")
include(":examples:bukkit-adventure-platform")
include(":examples:bukkit-chatgpt", JavaVersion.VERSION_11)
include(":examples:minestom", JavaVersion.VERSION_17)
include(":examples:velocity", JavaVersion.VERSION_11)
include(":examples:sponge")
include(":examples:fabric", JavaVersion.VERSION_17)

fun include(projectPath: String, version: JavaVersion) {
    if (!JavaVersion.current().isCompatibleWith(version)) {
        println("Skipping $projectPath because of incompatible Java version, required: $version")
        return
    }

    include(projectPath)
}

if (JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    pluginManagement {
        repositories {
            maven {
                name = "Fabric"
                url = uri("https://maven.fabricmc.net/")
            }
            mavenCentral()
            gradlePluginPortal()
        }
    }
}