package dev.rollczi.litecommands;

/**
 * Holds the version of LiteCommands.
 * Replaced by Gradle during build, see buildSrc/src/main/kotlin/litecommands-compile-variables.gradle.kts
 * see also <a href="https://github.com/KyoriPowered/blossom">Blossom</a>
 */
public final class LiteCommandsVariables {

    public static final String VERSION = "{litecommands-version}";
    public static final String BRANCH = "{litecommands-branch}";
    public static final String COMMIT = "{litecommands-commit}";

    private LiteCommandsVariables() {
    }

}
