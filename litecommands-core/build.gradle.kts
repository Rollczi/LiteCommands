dependencies {
    api("org.panda-lang:expressible:1.2.1")

    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.20")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testImplementation("org.awaitility:awaitility:4.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

val coreArtifact: String by rootProject.extra

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