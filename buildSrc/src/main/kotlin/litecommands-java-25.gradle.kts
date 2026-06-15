import org.gradle.api.JavaVersion

plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.toVersion("25")
    targetCompatibility = JavaVersion.toVersion("25")
}
