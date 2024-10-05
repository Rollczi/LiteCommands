plugins {
    `java-library`
}

tasks.withType<Test> {
    useJUnitPlatform()
    maxParallelForks = Runtime.getRuntime().availableProcessors()

    if (isTest()) {
        gradle.startParameter.taskRequests.forEach {
            if (it.args.contains("test") && !it.args.contains("--rerun")) {
                it.args.add("--rerun")
            }
        }
    }
}

fun isTest() = gradle.startParameter.taskNames.any { it.contains("test") }
