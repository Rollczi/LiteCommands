plugins {
    id("litecommands.java-conventions")
}

dependencies {
    api(project(":litecommands-core"))

    testImplementation(project(":litecommands-core").dependencyProject.sourceSets.test.get().output)
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation("org.awaitility:awaitility:4.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

val coreAnnotationsArtifact: String by extra

publishing {
    publications {
        create<MavenPublication>("maven") {
            this.artifactId = coreAnnotationsArtifact
            this.from(components["java"])
        }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}