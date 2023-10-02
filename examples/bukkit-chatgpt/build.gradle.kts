import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.2.0"
}

version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.panda-lang.org/releases/") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")

    // implementation("dev.rollczi:litecommands-bukkit:3.0.0-BETA-pre15") // <-- uncomment in your project
    // implementation("dev.rollczi:litecommands-chatgpt:3.0.0-BETA-pre15") // <-- uncomment in your project
    implementation(project(":litecommands-bukkit")) // don't use this line in your build.gradle
    implementation(project(":litecommands-chatgpt")) // don't use this line in your build.gradle

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}


bukkit {
    main = "dev.rollczi.example.bukkit.chatgpt.ExampleChatGptPlugin"
    apiVersion = "1.13"
    prefix = "ExamplePlugin"
    author = "Rollczi"
    name = "ExamplePlugin"
    version = "${project.version}"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("ExamplePlugin v${project.version}.jar")

    relocate("panda", "dev.rollczi.example.bukkit.libs.org.panda")
    relocate("org.panda_lang", "dev.rollczi.example.bukkit.libs.org.panda")
    relocate("dev.rollczi.litecommands", "dev.rollczi.example.bukkit.libs.dev.rollczi")
}

sourceSets {
    main {
    }
    test {
        java.setSrcDirs(emptyList<String>())
        resources.setSrcDirs(emptyList<String>())
    }
}

tasks {
    runServer {
        minecraftVersion("1.20.2")
    }
}
