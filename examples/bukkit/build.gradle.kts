plugins {
    id("java")
    id("com.github.johnrengelman.shadow")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT")
    // implementation("dev.rollczi.litecommands:bukkit:2.4.1") // <-- uncomment in your project
    implementation(project(":litecommands-bukkit")) // don't use this line in your build.gradle

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
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