plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
    application
}

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases")
}

dependencies {
    // implementation("dev.rollczi:litecommands-telegrambots:3.9.7") // <-- uncomment in your project
    implementation(project(":litecommands-telegrambots")) // don't use this line in your build.gradle

    compileOnly("org.telegram:telegrambots-longpolling:8.2.0")
    compileOnly("org.telegram:telegrambots-client:8.2.0")
}

application {
    mainClass = "dev.rollczi.example.telegrambots.TelegramExampleApplication"
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}