import com.google.common.io.Files

plugins {
    id("java-library")
    id("net.kyori.indra.git")
}

val input = file("src")
var output = File(project.layout.buildDirectory.get().asFile, "tmp/sources/java/")

val variablesInputFile = File(input, "dev/rollczi/litecommands/LiteCommandsVariables.java")
val variablesOutputFile = File(output, "dev/rollczi/litecommands/LiteCommandsVariables.java")
var variablesContent: String = variablesInputFile.readText()

tasks.compileJava {
    doFirst {
        if (!indraGit.isPresent) {
            throw IllegalStateException("indra-git is not present")
        }

        val version = project.version.toString()
        val branchName = indraGit.branchName()
            ?: System.getenv("GIT_BRANCH")
            ?: throw IllegalStateException("branch name is null")

        val commitHash = indraGit.commit()?.name
            ?: System.getenv("GIT_COMMIT")
            ?: throw IllegalStateException("commit is null")

        val newContent = variablesContent
            .replace("{litecommands-version}", version)
            .replace("{litecommands-branch}", branchName)
            .replace("{litecommands-commit}", commitHash)


        if (output.exists()) {
            // Remove the output directory if it exists to prevent any possible conflicts
            deleteDirectory(output)
        }

        output.mkdirs();
        output = output.getCanonicalFile()


        setSource(output)
        // copy all files

        input.walkTopDown().forEach {
            if (it.isFile) {
                val relativePath = it.relativeTo(input)
                val outputFile = File(output, relativePath.path.toString())

                Files.createParentDirs(outputFile)

                if (outputFile.path.equals(variablesOutputFile.path)) {
                    outputFile.createNewFile()
                    outputFile.writeText(newContent)
                    return@forEach
                }


                it.copyTo(outputFile)
            }
        }
    }
}

fun deleteDirectory(directory: File): Boolean {
    if (directory.exists()) {
        val files = directory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    deleteDirectory(file)
                } else {
                    file.delete()
                }
            }
        }
    }

    return directory.delete()
}