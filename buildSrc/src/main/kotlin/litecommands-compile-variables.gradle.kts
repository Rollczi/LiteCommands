plugins {
    id("java-library")
    id("net.kyori.indra.git")
}

val litecommandsVariables = "src/dev/rollczi/litecommands/LiteCommandsVariables.java"
val sourceFile = file(litecommandsVariables)
var content: String = sourceFile.readText()

tasks.compileJava {
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

    val newContent = content
        .replace("{litecommands-version}", version)
        .replace("{litecommands-branch}", branchName)
        .replace("{litecommands-commit}", commitHash)

    doFirst {
        sourceFile.writeText(newContent)
    }
}

tasks.classes {
    doLast {
        sourceFile.writeText(content)
    }
}
