plugins {
    id("java-library")
}

dependencies {
    testImplementation(project(":litecommands-unit"))

    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation("org.awaitility:awaitility:4.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
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
    dependencies {
        extension.projects().forEach {
            testImplementation(it.sourceSets.test.get().output)
        }

        extension.dependencies().forEach {
            testImplementation(it)
        }
    }
}