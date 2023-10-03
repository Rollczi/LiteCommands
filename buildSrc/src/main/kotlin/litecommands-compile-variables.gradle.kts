plugins {
    id("java-library")
    id("net.kyori.blossom")
    id("net.kyori.indra.git")
}

blossom {
    indraGit {
        if (!isPresent) {
            throw IllegalStateException("indra-git is not present")
        }

        val litecommandsVariables = "src/dev/rollczi/litecommands/LiteCommandsVariables.java"

        val version = project.version.toString()
        val branchName = branchName() ?: throw IllegalStateException("branch name is null")

        val commit = commit() ?: throw IllegalStateException("commit is null")
        val commitHash = commit.name

        replaceToken("{litecommands-version}", version, litecommandsVariables)
        replaceToken("{litecommands-branch}", branchName, litecommandsVariables)
        replaceToken("{litecommands-commit}", commitHash, litecommandsVariables)
    }
}