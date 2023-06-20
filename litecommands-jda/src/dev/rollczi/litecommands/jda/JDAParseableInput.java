package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.input.ParsableInputMatcher;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.argument.parser.LiteParseException;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.invalid.InvalidUsage;
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

    class JDAInputMatcher extends AbstractJDAInput<JDAInputMatcher>.AbstractJDAMatcher implements ParsableInputMatcher<JDAInputMatcher> {
        private final Set<String> consumedArguments = new HashSet<>();

        JDAInputMatcher() {}

        JDAInputMatcher(int routePosition) {
            this.routePosition = routePosition;
        }

        @Override
        public <SENDER, PARSED> ParseResult<PARSED> nextArgument(Invocation<SENDER> invocation, Argument<PARSED> argument, ParserSet<SENDER, PARSED> parserSet) {
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
                return this.parseInput(invocation, argument, parserSet, input);
            }
            catch (LiteParseException exception) {
                return this.parseInput(invocation, argument, parserSet, RawInput.of(optionMapping.getAsString().split(" ")));
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

        @SuppressWarnings("unchecked")
        private <SENDER, INPUT, OUT> ParseResult<OUT> parseInput(Invocation<SENDER> invocation, Argument<OUT> argument, ParserSet<SENDER, OUT> parserSet, INPUT input) {
            Class<INPUT> inputType = (Class<INPUT>) input.getClass();
            Parser<SENDER, INPUT, OUT> parser = parserSet.getParser(inputType)
                .orElseThrow(() -> new LiteParseException("No parser for input type " + inputType.getName()));

            return parser.parse(invocation, argument, input);
        }

        @Override
        public JDAInputMatcher copy() {
            return new JDAInputMatcher(routePosition);
        }

        @Override
        public EndResult endMatch() {
            if (consumedArguments.size() != arguments.size()) {
                return EndResult.failed(InvalidUsage.Cause.TOO_FEW_ARGUMENTS);
            }

            return EndResult.success();
        }
    }

}
