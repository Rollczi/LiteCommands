import org.gradle.api.JavaVersion.*

rootProject.name = "LiteCommands"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
    }
}

// core
include(":litecommands-core")
include(":litecommands-annotations")
include(":litecommands-programmatic")
include(":litecommands-framework")
include(":litecommands-unit")

// extensions
include(":litecommands-adventure")
include(":litecommands-adventure-platform", tests = false)
include(":litecommands-chatgpt")
include(":litecommands-jakarta")
include(":litecommands-luckperms")

// platforms
include(":litecommands-velocity", tests = false)
include(":litecommands-bungee", tests = false)
include(":litecommands-bukkit")
include(":litecommands-folia", VERSION_21, tests = false)
include(":litecommands-minestom", VERSION_21)
include("litecommands-jda")
include(":litecommands-sponge", VERSION_21, tests = false)
include(":litecommands-fabric", tests = false)
include(":litecommands-telegrambots", tests = false)

// examples
include(":examples:bukkit", tests = false)
include(":examples:bukkit-adventure-platform", tests = false)
include(":examples:bukkit-chatgpt", tests = false)
include(":examples:minestom", VERSION_21, tests = false)
include(":examples:paper", VERSION_21, tests = false)
include(":examples:fabric", tests = false)
include(":examples:jda", tests = false)
include(":examples:sponge", VERSION_21, tests = false)
include(":examples:telegrambots", tests = false)
include(":examples:velocity", tests = false)

fun include(project: String, java: JavaVersion = VERSION_17, tests: Boolean = true) {
    compatibleWith("including $project", java, tests) {
        settings.include(project)
    }
}

fun compatibleWith(action: String, java: JavaVersion, tests: Boolean = true, block: () -> Unit) {
    if (!current().isCompatibleWith(java)) {
        println("Skipping $action because of incompatible Java version, required: $java")
        return
    }

    if (isTest() && !tests) {
        println("Skipping $action while running tests")
        return
    }

    block()
}

fun isTest() = settings.gradle.startParameter.taskRequests.any {
    it.args.any { arg -> arg.endsWith("test") || arg.endsWith("testClasses") }
}