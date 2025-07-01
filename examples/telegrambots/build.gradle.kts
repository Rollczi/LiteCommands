plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.8"
    application
}

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases")
}

dependencies {
    // implementation("dev.rollczi:litecommands-telegrambots:3.10.0") // <-- uncomment in your project
    implementation(project(":litecommands-telegrambots")) // don't use this line in your build.gradle

    implementation("org.telegram:telegrambots-longpolling:9.0.0")
    implementation("org.telegram:telegrambots-client:9.0.0")
}

application {
    mainClass = "dev.rollczi.example.telegrambots.TelegramExampleApplication"
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}