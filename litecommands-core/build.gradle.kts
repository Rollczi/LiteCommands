dependencies {
    api("org.panda-lang:expressible:1.1.19")
    api("org.panda-lang.utilities:di:1.5.1")

    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
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