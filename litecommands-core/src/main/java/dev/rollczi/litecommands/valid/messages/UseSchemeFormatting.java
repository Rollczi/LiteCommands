package dev.rollczi.litecommands.valid.messages;

import panda.utilities.text.Joiner;

import java.util.List;
import java.util.function.Function;

public interface UseSchemeFormatting {

    /**
     * Slash of command
     *
     * Example: For "/"
     * Result: /command
     *
     * Example: For "#"
     * Result: #command
     *
     * @return Characters used before the command
     */
    String commandSlash();

    /**
     * Formatting of the command
     *
     * Example: For (string -> string)
     * Result: /command
     *
     * Example: For (string -> "-" + string + "-")
     * Result: /-command-
     *
     * @return Function that formats the command
     */
    Function<String, String> commandFormat();

    /**
     * Formatting of the subcommand
     *
     * Example: For (string -> string)
     * Result: /command subcommand
     *
     * Example: For (string -> "-" + string + "-")
     * Result: /command -subcommand-
     *
     * @return Function that formats the subcommand
     */
    Function<String, String> subcommandFormat();

    /**
     * Formatting for next subcommand
     *
     * Example: For (list -> Joiner.on("/").join(list).toString())
     * Result: /command subcommand/otherSubcommand
     *
     * Example: For (list -> Joiner.on("|").join(list).toString())
     * Result: /command subcommand|otherSubcommand
     *
     * Example: For (list -> "[" + Joiner.on("/").join(list).toString() + ">")
     * Result: /command [subcommand/otherSubcommand]
     *
     * @return Function that formats the next subcommand
     */
    Function<List<String>, String> nextSubcommandsFormat();

    /**
     * Formatting for the parameter
     *
     * Example: For (string -> string)
     * Result: /command subcommand parameter
     *
     * Example: For (string -> "<" + string + ">")
     * Result: /command subcommand <parameter>
     *
     * @return Function that formats the parameter
     */
    Function<String, String> parameterFormat();

    /**
     * Formatting for the optional parameter
     *
     * Example: For (string -> string)
     * Result: /command subcommand <parameter> optionalParameter
     *
     * Example: For (string -> "[" + string + "]")
     * Result: /command subcommand <parameter> [optionalParameter]
     *
     * @return Function that formats the optional parameter
     */
    Function<String, String> optionalParameterFormat();

    UseSchemeFormatting NORMAL = new LiteUseSchemeFormatting(
            "/",
            Function.identity(),
            Function.identity(),
            subCommands -> Joiner.on("/").join(subCommands).toString(),
            param -> "<" + param + ">",
            param -> "[" + param + "]"
    );

    UseSchemeFormatting REVERT_OPTIONAL = new LiteUseSchemeFormatting(
            "/",
            Function.identity(),
            Function.identity(),
            subCommands -> Joiner.on("/").join(subCommands).toString(),
            param -> "[" + param + "]",
            param -> "<" + param + ">"
    );

    UseSchemeFormatting PIPE = new LiteUseSchemeFormatting(
            "/",
            Function.identity(),
            Function.identity(),
            subCommands -> Joiner.on("|").join(subCommands).toString(),
            param -> "<" + param + ">",
            param -> "[" + param + "]"
    );

    UseSchemeFormatting PIPE_AND_REVERT_OPTIONAL = new LiteUseSchemeFormatting(
            "/",
            Function.identity(),
            Function.identity(),
            subCommands -> Joiner.on("|").join(subCommands).toString(),
            param -> "[" + param + "]",
            param -> "<" + param + ">"
    );

    class LiteUseSchemeFormatting implements UseSchemeFormatting  {

        private final String commandSlash;
        private final Function<String, String> commandFormat;
        private final Function<String, String> subcommandFormat;
        private final Function<List<String>, String> nextSubcommandsFormat;
        private final Function<String, String> parameterFormat;
        private final Function<String, String> optionalParameterFormat;

        public LiteUseSchemeFormatting(String commandSlash, Function<String, String> commandFormat, Function<String, String> subcommandFormat, Function<List<String>, String> nextSubcommandsFormat, Function<String, String> parameterFormat, Function<String, String> optionalParameterFormat) {
            this.commandSlash = commandSlash;
            this.commandFormat = commandFormat;
            this.subcommandFormat = subcommandFormat;
            this.nextSubcommandsFormat = nextSubcommandsFormat;
            this.parameterFormat = parameterFormat;
            this.optionalParameterFormat = optionalParameterFormat;
        }

        @Override
        public String commandSlash() {
            return commandSlash;
        }

        @Override
        public Function<String, String> commandFormat() {
            return commandFormat;
        }

        @Override
        public Function<String, String> subcommandFormat() {
            return subcommandFormat;
        }

        @Override
        public Function<List<String>, String> nextSubcommandsFormat() {
            return nextSubcommandsFormat;
        }

        @Override
        public Function<String, String> parameterFormat() {
            return parameterFormat;
        }

        @Override
        public Function<String, String> optionalParameterFormat() {
            return optionalParameterFormat;
        }

    }

}
