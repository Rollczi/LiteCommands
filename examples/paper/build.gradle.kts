plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.1"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.panda-lang.org/releases/") }
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")

    // implementation("dev.rollczi:litecommands-bukkit:3.10.9") // <-- uncomment in your project
    implementation(project(":litecommands-bukkit")) // don't use this line in your build.gradle
}

val pluginName = "ExamplePlugin"
val packageName = "dev.rollczi.example.paper"

paper {
    main = "$packageName.$pluginName"
    apiVersion = "1.19"
    author = "Rollczi"
    name = pluginName
    version = "1.0.0"
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
