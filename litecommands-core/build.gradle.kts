plugins {
    id("litecommands.java-conventions")
}

dependencies {
    api("org.panda-lang:expressible:1.3.0")
    api("org.jetbrains:annotations:24.0.0")

    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation("org.awaitility:awaitility:4.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

val coreArtifact: String by extra

publishing {
    publications {
        create<MavenPublication>("maven") {
            this.artifactId = coreArtifact
            this.from(components["java"])
        }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}