import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.add
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    testImplementation(project(":litecommands-unit"))
    testImplementation(kotlin("stdlib-jdk8"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT_JUPITER}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${Versions.JUNIT_JUPITER}")
    testImplementation("org.assertj:assertj-core:${Versions.ASSERTJ}")
    testImplementation("org.awaitility:awaitility:${Versions.AWAITILITY}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT_JUPITER}")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    maxParallelForks = Runtime.getRuntime().availableProcessors()

    tasks.withType<JavaCompile>().configureEach {
        options.isFork = true
        options.compilerArgs.add("-parameters")
    }
}

sourceSets.test {
    java.setSrcDirs(listOf("test"))
    resources.setSrcDirs(emptyList<String>())
}

kotlin {
    jvmToolchain(java.targetCompatibility.ordinal + 1)
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