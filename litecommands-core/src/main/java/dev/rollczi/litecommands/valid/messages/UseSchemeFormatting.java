package dev.rollczi.litecommands.valid.messages;

import java.util.function.Function;

public interface UseSchemeFormatting {

    String commandSlash();

    Function<String, String> commandFormat();

    Function<String, String> subcommandFormat();

    Function<String, String> parameterFormat();

    UseSchemeFormatting NORMAL =
            new LiteUseSchemeFormatting("/", Function.identity(), Function.identity(), param -> "<" + param + ">");

    class LiteUseSchemeFormatting implements UseSchemeFormatting  {

        private final String commandSlash;
        private final Function<String, String> commandFormat;
        private final Function<String, String> subcommandFormat;
        private final Function<String, String> parameterFormat;

        public LiteUseSchemeFormatting(String commandSlash, Function<String, String> commandFormat, Function<String, String> subcommandFormat, Function<String, String> parameterFormat) {
            this.commandSlash = commandSlash;
            this.commandFormat = commandFormat;
            this.subcommandFormat = subcommandFormat;
            this.parameterFormat = parameterFormat;
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
        public Function<String, String> parameterFormat() {
            return parameterFormat;
        }

    }

}
