
task("test") {
    project.allprojects.forEach {
        dependsOn(it.tasks.withType<Test>())
    }
}