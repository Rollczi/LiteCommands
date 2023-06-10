
tasks.withType<Test> {
    val task = this

    subprojects {
        val project = this
        val withType: TaskCollection<Test> = project.tasks.withType<Test>()

        if (withType.isEmpty()) {
            return@subprojects
        }

        task.dependsOn("$path:test")
    }

}