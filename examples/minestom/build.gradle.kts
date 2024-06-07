plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.0.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases/")
    maven("https://jitpack.io")
}

dependencies {
    implementation("dev.hollowcube:minestom-ce:5347c0b11f")
    implementation("net.kyori:adventure-text-minimessage:4.15.0")

    // implementation("dev.rollczi:litecommands-minestom:3.4.2") // <-- uncomment in your project
    implementation(project(":litecommands-minestom")) // don't use this line in your build.gradle
}

tasks.shadowJar {
    archiveFileName.set("ExampleMinestom v${project.version}.jar")
}

sourceSets.test {
    java.setSrcDirs(emptyList<String>())
    resources.setSrcDirs(emptyList<String>())
}
