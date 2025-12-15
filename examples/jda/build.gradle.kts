plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.0"
    application
}

group = "dev.rollczi"
version = "3.10.6"

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases")
}

dependencies {
    // implementation("dev.rollczi:litecommands-jda:3.10.6") // <-- uncomment in your project
    implementation(project(":litecommands-jda")) // don't use this line in your build.gradle

    implementation("net.dv8tion:JDA:6.2.0")
}

application {
    mainClass = "dev.rollczi.example.jda.JDAExampleApplication"
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}