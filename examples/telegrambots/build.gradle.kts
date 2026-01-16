plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.1"
    application
}

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases")
}

dependencies {
    // implementation("dev.rollczi:litecommands-telegrambots:3.10.9") // <-- uncomment in your project
    implementation(project(":litecommands-telegrambots")) // don't use this line in your build.gradle

    implementation("org.telegram:telegrambots-longpolling:${Versions.TELEGRAM_BOTS}")
    implementation("org.telegram:telegrambots-client:${Versions.TELEGRAM_BOTS}")
}

application {
    mainClass = "dev.rollczi.example.telegrambots.TelegramExampleApplication"
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}