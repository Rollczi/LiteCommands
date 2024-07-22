package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.ParserChainAccessor;
import dev.rollczi.litecommands.argument.parser.ParserChained;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.ReflectUtil;
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
        public <SENDER, PARSED> ParseResult<PARSED> nextArgument(Invocation<SENDER> invocation, Argument<PARSED> argument, Parser<SENDER, PARSED> parser) {
            OptionMapping optionMapping = arguments.get(argument.getName());

            if (optionMapping == null) {
                return ParseResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
            }

            Class<PARSED> outType = argument.getWrapperFormat().getParsedType();
            consumedArguments.add(argument.getName());

            Object input = command.mapArgument(toRoute(argument.getName()), optionMapping);

            if (input == null) {
                input = RawInput.of(optionMapping.getAsString().split(" "));
            }

            if (ReflectUtil.instanceOf(input, outType)) {
                return ParseResult.success((PARSED) input);
            }

            try {
                //TODO: implement parse custom Input to PARSED
                throw new LiteJDAParseException("Cannot parse input");
            }
            catch (LiteJDAParseException exception) {
                return parser.parse(invocation, argument, RawInput.of(optionMapping.getAsString().split(" ")));
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
        public EndResult endMatch() {
            if (consumedArguments.size() != arguments.size()) {
                return EndResult.failed(InvalidUsage.Cause.MISSING_ARGUMENT);
            }

            return EndResult.success();
        }

    }

    private static class LiteJDAParseException extends LiteCommandsException {
        public LiteJDAParseException(String message) {
            super(message);
        }
    }

}
