package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.ReflectUtil;
import java.util.function.Supplier;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class JDAParseableInput extends AbstractJDAInput<JDAParseableInput.JDAInputMatcher> implements ParseableInput<JDAParseableInput.JDAInputMatcher> {

    private final JDACommandTranslator.JDALiteCommand command;

    JDAParseableInput(List<String> routes, Map<String, OptionMapping> arguments, JDACommandTranslator.JDALiteCommand command) {
        super(routes, arguments);
        this.command = command;
    }

    @Override
    public JDAInputMatcher createMatcher() {
        return new JDAInputMatcher();
    }

    class JDAInputMatcher extends AbstractJDAInput<JDAInputMatcher>.AbstractJDAMatcher implements ParseableInputMatcher<JDAInputMatcher> {
        private final Set<String> consumedArguments = new HashSet<>();

        JDAInputMatcher() {}

        JDAInputMatcher(int routePosition) {
            this.routePosition = routePosition;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <SENDER, T> ParseResult<T> nextArgument(Invocation<SENDER> invocation, Argument<T> argument, Supplier<Parser<SENDER, T>> parserProvider) {
            CommandInteractionPayload payload = invocation.context().get(CommandInteractionPayload.class)
                .orElseThrow(() -> new LiteCommandsException("Invocation context does not contain CommandInteractionPayload"));

            OptionMapping option = payload.getOption(argument.getName());

            if (option == null) {
                return ParseResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
            }

            Class<T> type = argument.getType().getRawType();
            Object input = command.mapArgument(toRoute(argument.getName()), option);

            consumedArguments.add(argument.getName());

            if (ReflectUtil.instanceOf(input, type)) {
                return ParseResult.success((T) input);
            }

            try {
                Parser<SENDER, T> parser = parserProvider.get();

                return parser.parse(invocation, argument, RawInput.of(option.getAsString().split(" ")));
            }
            catch (IllegalArgumentException exception) {
                if (input != null) {
                    throw new LiteCommandsException("Input: " + input + " is not instance of " + type.getSimpleName());
                }

                throw new LiteCommandsException("Cannot parse argument: " + argument.getName(), exception);
            }
        }

        private JDACommandTranslator.JDARoute toRoute(String argumentName) {
            if (routes.isEmpty()) {
                return new JDACommandTranslator.JDARoute("", "", argumentName);
            }

            if (routes.size() == 1) {
                return new JDACommandTranslator.JDARoute("", routes.get(0), argumentName);
            }

            if (routes.size() == 2) {
                return new JDACommandTranslator.JDARoute(routes.get(0), routes.get(1), argumentName);
            }

            throw new IllegalArgumentException("Cannot convert to route");
        }

        @Override
        public JDAInputMatcher copy() {
            return new JDAInputMatcher(routePosition);
        }

        @Override
        public EndResult endMatch(boolean isStrict) {
            if (consumedArguments.size() != arguments.size() && isStrict) {
                return EndResult.failed(InvalidUsage.Cause.MISSING_ARGUMENT);
            }

            return EndResult.success();
        }

    }

}
