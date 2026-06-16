import org.gradle.api.tasks.testing.Test

plugins {
    id("java-library")
}

dependencies {
    testImplementation(project(":litecommands-unit"))

    testImplementation(platform("org.junit:junit-bom:${Versions.JUNIT_JUPITER}"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core:${Versions.ASSERTJ}")
    testImplementation("org.awaitility:awaitility:${Versions.AWAITILITY}")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    maxParallelForks = Runtime.getRuntime().availableProcessors()

    tasks.withType<JavaCompile>().configureEach {
        options.isFork = true
        options.compilerArgs.add("-parameters")
    }
}

tasks.named<JavaCompile>("compileTestJava") {
    if (java.targetCompatibility < JavaVersion.VERSION_17) {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}

sourceSets.test {
    java.setSrcDirs(listOf("test"))
    resources.setSrcDirs(emptyList<String>())
}

open class TestImplementation {

    private val projects = mutableListOf<Project>()
    private val dependencies = mutableListOf<String>()

    fun useTestModuleOf(project: Project) {
        projects.add(project)
    }

    fun use(dependency: String) {
        dependencies.add(dependency)
    }

    internal fun projects(): List<Project> {
        return projects
    }

    internal fun dependencies(): List<String> {
        return dependencies
    }

}

val extension = extensions.create<TestImplementation>("litecommandsUnit")

afterEvaluate {
    extension.projects().forEach { currentProject ->
        evaluationDependsOn(currentProject.path)
    }

    dependencies {
        extension.projects().forEach {
            testImplementation(it.sourceSets.test.get().output)
        }

        extension.dependencies().forEach {
            testImplementation(it)
        }
    }
}