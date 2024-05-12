plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("xyz.jpenilla.run-velocity") version "2.2.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.panda-lang.org/releases/") }
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")

    // implementation("dev.rollczi:litecommands-velocity:3.4.1") // <-- uncomment in your project
    implementation(project(":litecommands-velocity")) // don't use this line in your build.gradle
}

val pluginName = "ExampleVelocityPlugin"
val packageName = "dev.rollczi.example.velocity"

tasks.shadowJar {
    archiveFileName.set("$pluginName v${project.version}.jar")

    listOf(
        "panda.std",
        "dev.rollczi.litecommands",
    ).forEach { relocate(it, "$packageName.libs.$it") }
}

sourceSets.test {
    java.setSrcDirs(emptyList<String>())
    resources.setSrcDirs(emptyList<String>())
}

tasks.runVelocity {
    velocityVersion("3.2.0-SNAPSHOT")
}
