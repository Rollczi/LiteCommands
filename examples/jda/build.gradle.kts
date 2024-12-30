plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
    application
}

group = "dev.rollczi"
version = "3.9.5"

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases")
    maven("https://repo.eternalcode.pl/snapshots")
}

dependencies {
    // implementation("dev.rollczi:litecommands-jda:3.9.5") // <-- uncomment in your project
    implementation(project(":litecommands-jda")) // don't use this line in your build.gradle

    implementation("net.dv8tion:JDA:5.2.2")
}

application {
    mainClass = "dev.rollczi.example.jda.JDAExampleApplication"
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}