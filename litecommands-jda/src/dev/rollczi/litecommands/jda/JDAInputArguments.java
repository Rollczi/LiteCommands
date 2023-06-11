package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.parser.ArgumentParseException;
import dev.rollczi.litecommands.argument.parser.ArgumentParser;
import dev.rollczi.litecommands.argument.parser.ArgumentParserSet;
import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.argument.input.InputArgumentsMatcher;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class JDAInputArguments implements InputArguments<JDAInputArguments.JDAInputMatcher> {

    private final List<String> routes = new ArrayList<>();
    private final Map<String, OptionMapping> arguments = new HashMap<>();
    private final JDACommandTranslator.JDALiteCommand command;

    JDAInputArguments(List<String> routes, Map<String, OptionMapping> arguments, JDACommandTranslator.JDALiteCommand command) {
        this.command = command;
        this.routes.addAll(routes);
        this.arguments.putAll(arguments);
    }

    @Override
    public JDAInputMatcher createMatcher() {
        return new JDAInputMatcher();
    }

    @Override
    public List<String> asList() {
        List<String> list = new ArrayList<>(routes);

        for (Map.Entry<String, OptionMapping> entry : arguments.entrySet()) {
            list.add(entry.getValue().getAsString());
        }

        return list;
    }

    class JDAInputMatcher implements InputArgumentsMatcher<JDAInputMatcher> {

        private int routePosition = 0;
        private final Set<String> consumedArguments = new HashSet<>();

        JDAInputMatcher() {}

        JDAInputMatcher(int routePosition) {
            this.routePosition = routePosition;
        }

        @Override
        public <SENDER, PARSED> ArgumentResult<PARSED> nextArgument(Invocation<SENDER> invocation, Argument<PARSED> argument, ArgumentParserSet<SENDER, PARSED> parserSet) {
            OptionMapping optionMapping = arguments.get(argument.getName());

            if (optionMapping == null) {
                return ArgumentResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
            }

            Class<PARSED> outType = argument.getWrapperFormat().getParsedType();
            consumedArguments.add(argument.getName());

            Object input = command.mapArgument(toRoute(argument.getName()), optionMapping);

            if (input == null) {
                input = optionMapping.getAsString();
            }

            if (outType.isAssignableFrom(input.getClass())) {
                return ArgumentResult.success((PARSED) input);
            }

            return this.parseInput(invocation, argument, parserSet, input);
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
        private <SENDER, INPUT, OUT> ArgumentResult<OUT> parseInput(Invocation<SENDER> invocation, Argument<OUT> argument, ArgumentParserSet<SENDER, OUT> parserSet, INPUT input) {
            Class<INPUT> inputType = (Class<INPUT>) input.getClass();
            ArgumentParser<SENDER, INPUT, OUT> parser = parserSet.getParser(inputType)
                .orElseThrow(() -> new ArgumentParseException("No parser for input type " + inputType.getName()));

            return parser.parse(invocation, argument, input);
        }

        @Override
        public boolean hasNextRoute() {
            return routePosition < routes.size();
        }

        @Override
        public String showNextRoute() {
            return routes.get(routePosition);
        }

        @Override
        public String nextRoute() {
            return routes.get(routePosition++);
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
