import org.gradle.api.JavaVersion

plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}
