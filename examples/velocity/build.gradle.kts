plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.1"
    id("xyz.jpenilla.run-velocity") version "3.0.2"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.panda-lang.org/releases/") }
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")

    // implementation("dev.rollczi:litecommands-velocity:3.10.9") // <-- uncomment in your project
    implementation(project(":litecommands-velocity")) // don't use this line in your build.gradle
}

val pluginName = "ExampleVelocityPlugin"
val packageName = "dev.rollczi.example.velocity"

tasks.shadowJar {
    archiveFileName.set("$pluginName v${project.version}.jar")

    listOf(
        "dev.rollczi.litecommands",
    ).forEach { relocate(it, "$packageName.libs.$it") }
}

sourceSets.test {
    java.setSrcDirs(emptyList<String>())
    resources.setSrcDirs(emptyList<String>())
}

tasks.runVelocity {
    velocityVersion("3.3.0-SNAPSHOT")
}
