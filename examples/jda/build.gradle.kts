plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.8"
    application
}

group = "dev.rollczi"
version = "3.10.4"

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases")
}

dependencies {
    // implementation("dev.rollczi:litecommands-jda:3.10.4") // <-- uncomment in your project
    implementation(project(":litecommands-jda")) // don't use this line in your build.gradle

    implementation("net.dv8tion:JDA:5.6.1")
}

application {
    mainClass = "dev.rollczi.example.jda.JDAExampleApplication"
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}