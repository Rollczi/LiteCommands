plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.2"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases/")
    maven("https://jitpack.io")
}

dependencies {
    implementation("net.minestom:minestom:${Versions.MINESTOM}")
    implementation("net.kyori:adventure-text-minimessage:4.25.0")

    // implementation("dev.rollczi:litecommands-minestom:3.10.6") // <-- uncomment in your project
    implementation(project(":litecommands-minestom")) // don't use this line in your build.gradle
}

tasks.shadowJar {
    archiveFileName.set("ExampleMinestom v${project.version}.jar")
}

sourceSets.test {
    java.setSrcDirs(emptyList<String>())
    resources.setSrcDirs(emptyList<String>())
}
