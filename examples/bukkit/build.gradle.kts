plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.panda-lang.org/releases/") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")

    // implementation("dev.rollczi.litecommands:bukkit:2.8.6") // <-- uncomment in your project
    // implementation("dev.rollczi.litecommands:core-annotations:2.8.6") // <-- uncomment in your project
    implementation(project(":litecommands-bukkit")) // don't use this line in your build.gradle
    implementation(project(":litecommands-core-annotations")) // don't use this line in your build.gradle

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}


bukkit {
    main = "dev.rollczi.example.bukkit.ExamplePlugin"
    apiVersion = "1.13"
    prefix = "ExamplePlugin"
    author = "Rollczi"
    name = "ExamplePlugin"
    version = "${project.version}"
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveFileName.set("ExamplePlugin v${project.version}.jar")

    mergeServiceFiles()
    minimize()

    relocate("panda", "dev.rollczi.example.bukkit.libs.org.panda")
    relocate("org.panda_lang", "dev.rollczi.example.bukkit.libs.org.panda")
    relocate("dev.rollczi.litecommands", "dev.rollczi.example.bukkit.libs.dev.rollczi")
}