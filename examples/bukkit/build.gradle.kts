plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

version = "3.10.9"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.panda-lang.org/releases/") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")

    // implementation("dev.rollczi:litecommands-bukkit:3.10.9") // <-- uncomment in your project
    // implementation("dev.rollczi:litecommands-folia:3.10.9") // <-- uncomment in your project
    implementation(project(":litecommands-bukkit")) // don't use this line in your build.gradle
    implementation(project(":litecommands-folia")) // don't use this line in your build.gradle
}

val pluginName = "ExamplePlugin"
val packageName = "dev.rollczi.example.bukkit"

bukkit {
    main = "$packageName.$pluginName"
    apiVersion = "1.13"
    author = "Rollczi"
    name = pluginName
    version = "${project.version}"
    foliaSupported = true
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.shadowJar {
    archiveFileName.set("$pluginName v${project.version}.jar")

    listOf(
        "dev.rollczi.litecommands",
    ).forEach { relocate(it, "$packageName.libs.$it") }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
    options.release = 21
}

sourceSets.test {
    java.setSrcDirs(emptyList<String>())
    resources.setSrcDirs(emptyList<String>())
}

tasks.runServer {
    minecraftVersion("1.21.4")
    allJvmArgs = listOf("-DPaper.IgnoreJavaVersion=true")
}
