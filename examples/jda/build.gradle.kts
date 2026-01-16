plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.1"
    application
}

group = "dev.rollczi"
version = "3.10.9"

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases")
}

dependencies {
    // implementation("dev.rollczi:litecommands-jda:3.10.9") // <-- uncomment in your project
    implementation(project(":litecommands-jda")) // don't use this line in your build.gradle

    implementation("net.dv8tion:JDA:6.3.0")
}

application {
    mainClass = "dev.rollczi.example.jda.JDAExampleApplication"
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}