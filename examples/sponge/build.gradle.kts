import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.1"
    id("org.spongepowered.gradle.plugin") version "2.3.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases/")
    maven("https://repo.spongepowered.org/repository/maven-releases/")
}

dependencies {
    // implementation("dev.rollczi:litecommands-sponge:3.3.3") // <-- uncomment in your project
    implementation(project(":litecommands-sponge")) // don't use this line in your build.gradle
}

val packageName = "dev.rollczi.example.sponge"

tasks.shadowJar {
    archiveFileName.set("ExampleSpongePlugin v${project.version}.jar")

    listOf(
        "dev.rollczi.litecommands",
    ).forEach { relocate(it, "$packageName.libs.$it") }
}

sourceSets.test {
    java.setSrcDirs(emptyList<String>())
    resources.setSrcDirs(emptyList<String>())
}

sponge {
    apiVersion("8.2.0")
    license("Apache License 2.0")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("${project.version}")
    }
    plugin("litecommands-example") {
        displayName("LiteCommands Example")
        entrypoint("$packageName.ExamplePlugin")
        description("LiteCommands example plugin")
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}
