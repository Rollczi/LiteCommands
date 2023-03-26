package dev.rollczi.litecommands.util;

public final class LiteCommandsUtil {

    private LiteCommandsUtil() {
    }

    /**
     * Checks if name and aliases are consistent with LiteCommands rules.
     * <p>
     * Name cannot be null or empty, cannot start or end with space, cannot contain two spaces in a row
     * Aliases cannot be null, cannot contain null, cannot contain empty, cannot contain two spaces in a row, cannot start or end with space, cannot contain different count of words than name
     * If name and aliases are consistent, returns true
     * </p>
     * <p>
     * Example:
     * </p>
     * <ul>
     *     <li>name: "command", aliases: ["command-1", "command-2"] - true</li>
     *     <li>name: "command", aliases: ["command-1", "command-2", "command-3"] - true</li>
     *     <li>name: "command subcommand", aliases: ["command-1", "command-2"] - throws IllegalArgumentException</li>
     *     <li>name: "command subcommand", aliases: ["command-1 subcommand"] - true</li>
     *     <li>name: "command", aliases: [] - true</li>
     *     <li>name: "", aliases: [] - false</li>
     *     <li>name: null, aliases: ["command-1", "command-2"] - throws NullPointerException</li>
     *     <li>name: "command", aliases: null - throws NullPointerException</li>
     *     <li>name: "command", aliases: ["command-1", null, "command-2"] - throws NullPointerException</li>
     * </ul>
     *
     * @param name    command name cannot be null or empty, cannot start or end with space, cannot contain two spaces in a row
     * @param aliases aliases name cannot be null, cannot contain null, cannot contain empty, cannot contain two spaces in a row, cannot start or end with space, cannot contain different count of words than name
     *
     * @return true if name and aliases are consistent, if name and aliases are empty returns false, otherwise throws exception
     *
     * @throws IllegalArgumentException if name or aliases are not consistent, if name or aliases contain two spaces in a row, if name or aliases start or end with space
     * @throws NullPointerException     if name or aliases are null
     * @see #checkName(String)
     */
    public static boolean checkConsistent(String name, String[] aliases) {
        if (!checkName(name)) {
            return false;
        }

        int countName = name.split(" ").length;

        for (String alias : aliases) {
            if (alias == null) {
                throw new NullPointerException("Alias cannot be null");
            }

            if (alias.split(" ").length != countName) {
                throw new IllegalArgumentException("Alias '" + alias + "' has different count of words than name '" + name + "'");
            }
        }

        for (String alias : aliases) {
            if (!checkName(alias)) {
                throw new IllegalStateException("Alias '" + alias + "' is not consistent");
            }
        }

        return true;
    }

    /**
     * Checks if name is consistent
     * <p>
     * Name cannot be null or empty, cannot start or end with space, cannot contain two spaces in a row
     * </p>
     * <p>
     * Example:
     * </p>
     * <ul>
     *     <li>name: "command" - true</li>
     *     <li>name: "command subcommand" - true</li>
     *     <li>name: null - throws NullPointerException</li>
     *     <li>name: "command  subcommand" - throws IllegalArgumentException</li>
     *     <li>name: " command" - throws IllegalArgumentException</li>
     *     <li>name: "command " - throws IllegalArgumentException</li>
     *     <li>name: "" - false</li>
     * </ul>
     *
     * @param name cannot be null or empty, cannot start or end with space, cannot contain two spaces in a row
     *
     * @return true if name is not empty, otherwise throws exception
     *
     * @throws IllegalArgumentException if name contains two spaces in a row, if name start or end with space
     * @throws NullPointerException     if name is null
     * @see #checkConsistent(String, String[])
     */
    public static boolean checkName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        if (name.isEmpty()) {
            return false;
        }

        if (name.contains("  ")) {
            throw new IllegalArgumentException("Name cannot contain two spaces in a row");
        }

        if (name.startsWith(" ") || name.endsWith(" ")) {
            throw new IllegalArgumentException("Name cannot start or end with space");
        }

        return true;
    }

}
