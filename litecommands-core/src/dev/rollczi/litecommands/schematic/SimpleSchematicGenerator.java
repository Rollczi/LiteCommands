package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.validator.ValidatorService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleSchematicGenerator<SENDER> implements SchematicGenerator<SENDER>, SchematicFormatProvider {

    private static final String SEPARATOR = " ";

    protected final SchematicFormat format;
    protected final ValidatorService<SENDER> validatorService;
    protected final ParserRegistry<SENDER> parserRegistry;

    public SimpleSchematicGenerator(SchematicFormat format, ValidatorService<SENDER> validatorService, ParserRegistry<SENDER> parserRegistry) {
        this.format = format;
        this.validatorService = validatorService;
        this.parserRegistry = parserRegistry;
    }

    @Override
    public Schematic generate(SchematicInput<SENDER> schematicInput) {
        List<String> schematics = generateRaw(schematicInput)
            .map(schematic -> format.prefix() + schematic.trim() + format.suffix())
            .distinct()
            .collect(Collectors.toList());

        return new Schematic(schematics);
    }

    @Override
    public SchematicFormat getFormat() {
        return format;
    }

    protected Stream<String> generateRaw(SchematicInput<SENDER> schematicInput) {
        CommandExecutor<SENDER> executor = schematicInput.getExecutor();
        String base = schematicInput.collectRoutes().stream()
            .map(route -> route.getName())
            .collect(Collectors.joining(SEPARATOR))
            + SEPARATOR;

        Stream<String> routeScheme = generateRoute(schematicInput, schematicInput.getLastRoute(), base);

        if (executor != null && isVisible(schematicInput, executor)) {
            Stream<String> executorScheme = Stream.of(base + generateExecutor(schematicInput, executor));
            return Stream.concat(routeScheme, executorScheme);
        }

        return routeScheme;
    }

    protected Stream<String> generateRoute(SchematicInput<SENDER> input, CommandRoute<SENDER> route, String base) {
        Stream<String> children = route.getChildren().stream()
            .flatMap(subRoute -> generateRoute(input, subRoute, base + subRoute.getName() + SEPARATOR));

        Stream<String> executors = route.getExecutors().stream()
            .filter(executor -> isVisible(input, executor))
            .map(executor -> base + generateExecutor(input, executor));

        return Stream.concat(executors, children);
    }

    protected String generateExecutor(SchematicInput<SENDER> input, CommandExecutor<SENDER> executor) {
        return executor.getArguments().stream()
            .map(argument -> String.format(generateArgumentFormat(input, argument), generateArgumentName(input, argument)))
            .collect(Collectors.joining(SEPARATOR));
    }

    protected String generateArgumentFormat(SchematicInput<SENDER> input, Argument<?> argument) {
        return this.isOptional(input, argument) ? format.optionalArgumentFormat() : format.argumentFormat();
    }

    protected String generateArgumentName(SchematicInput<SENDER> input, Argument<?> argument) {
        return argument.getName();
    }

    protected boolean isVisible(SchematicInput<SENDER> input, CommandExecutor<SENDER> executor) {
        return validatorService.validate(input.getInvocation(), executor).isContinue();
    }

    protected <T> boolean isOptional(SchematicInput<SENDER> input, Argument<T> argument) {
        Parser<SENDER, T> parser = parserRegistry.getParserOrNull(argument);

        if (parser != null) {
            Range range = parser.getRange(argument);

            if (range.getMin() == 0) {
                return true;
            }
        }

        return  argument.hasDefaultValue();
    }

}
