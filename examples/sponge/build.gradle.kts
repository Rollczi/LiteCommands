import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("org.spongepowered.gradle.plugin") version "2.2.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveFileName.set("ExampleSpongePlugin v${project.version}.jar")

    listOf(
        "panda",
//        "dev.rollczi.litecommands",
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
