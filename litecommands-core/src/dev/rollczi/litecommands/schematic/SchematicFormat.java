package dev.rollczi.litecommands.schematic;

public class SchematicFormat {

    private final String prefix;
    private final String suffix;
    private final String commandFormat;
    private final String argumentFormat;
    private final String optionalArgumentFormat;

    SchematicFormat(String prefix, String suffix, String commandFormat, String argumentFormat, String optionalArgumentFormat) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.commandFormat = commandFormat;
        this.argumentFormat = argumentFormat;
        this.optionalArgumentFormat = optionalArgumentFormat;
    }

    public String prefix() {
        return prefix;
    }

    public String suffix() {
        return suffix;
    }

    public String commandFormat() {
        return commandFormat;
    }

    public String argumentFormat() {
        return argumentFormat;
    }

    public String optionalArgumentFormat() {
        return optionalArgumentFormat;
    }

    public static SchematicFormat of(String prefix, String suffix, String commandFormat, String argumentFormat, String optionalArgumentFormat) {
        return new SchematicFormat(prefix, suffix, commandFormat, argumentFormat, optionalArgumentFormat);
    }

    public static SchematicFormat angleBrackets() {
        return new SchematicFormat("/", "", "%s", "<%s>", "[%s]");
    }

    public static SchematicFormat squareBrackets() {
        return new SchematicFormat("/", "", "%s", "[%s]", "<%s>");
    }

    public static SchematicFormat parentheses() {
        return new SchematicFormat("/", "", "%s", "(%s)", "(%s)");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String prefix = "/";
        private String suffix = "";
        private String command = "%s";
        private String argument = "<%s>";
        private String optionalArgument = "[%s]";

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public Builder command(String command) {
            this.command = command;
            return this;
        }

        public Builder argument(String argument) {
            this.argument = argument;
            return this;
        }

        public Builder optionalArgument(String optionalArgument) {
            this.optionalArgument = optionalArgument;
            return this;
        }

        public SchematicFormat build() {
            return new SchematicFormat(prefix, suffix, command, argument, optionalArgument);
        }

    }

}
