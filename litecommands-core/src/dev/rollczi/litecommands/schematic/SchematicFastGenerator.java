package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.validator.ValidatorService;
import java.util.LinkedHashSet;
import java.util.Set;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class SchematicFastGenerator<SENDER> implements SchematicGenerator<SENDER> {

    private static final String SEPARATOR = " ";

    protected final SchematicFastFormat fastFormat;
    protected final ValidatorService<SENDER> validatorService;
    protected final ParserRegistry<SENDER> parserRegistry;

    public SchematicFastGenerator(SchematicFastFormat fastFormat, ValidatorService<SENDER> validatorService, ParserRegistry<SENDER> parserRegistry) {
        this.fastFormat = fastFormat;
        this.validatorService = validatorService;
        this.parserRegistry = parserRegistry;
    }

    @Override
    public Schematic generate(SchematicInput<SENDER> schematicInput) {
        return new Schematic(generateRaw(schematicInput));
    }

    protected Set<String> generateRaw(SchematicInput<SENDER> schematicInput) {
        CommandExecutor<SENDER> executor = schematicInput.getExecutor();
        StringBuilder builder = new StringBuilder(fastFormat.prefix());

        for (CommandRoute<SENDER> route : schematicInput.collectRoutes()) {
            builder.append(route.getName()).append(SEPARATOR);
        }

        String base = builder.toString();

        Set<String> routeScheme = generateRoute(schematicInput, schematicInput.getLastRoute(), base);

        if (executor != null) {
            routeScheme.add(generateExecutor(base, schematicInput, executor));
        }

        return routeScheme;
    }

    protected Set<String> generateRoute(SchematicInput<SENDER> input, CommandRoute<SENDER> route, String base) {
        LinkedHashSet<String> schematics = new LinkedHashSet<>();

        for (CommandRoute<SENDER> subRoute : route.getChildren()) {
            schematics.addAll(generateRoute(input, subRoute, base + subRoute.getName() + SEPARATOR));
        }

        for (CommandExecutor<SENDER> executor : route.getExecutors()) {
            if (isVisible(input, executor)) {
                schematics.add(generateExecutor(base, input, executor));
            }
        }

        return schematics;
    }

    protected String generateExecutor(String base, SchematicInput<SENDER> input, CommandExecutor<SENDER> executor) {
        StringBuilder builder = new StringBuilder(base);

        for (Argument<?> argument : executor.getArguments()) {
            String argumentName = generateArgumentName(input, argument);

            builder
                .append(generateArgumentFormat(input, argument, argumentName))
                .append(SEPARATOR);
        }

        if (!executor.getArguments().isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }

    protected String generateArgumentFormat(SchematicInput<SENDER> input, Argument<?> argument, String argumentName) {
        return this.isOptional(input, argument)
            ? fastFormat.optionalArgumentStart().concat(argumentName).concat(fastFormat.optionalArgumentEnd())
            : fastFormat.argumentStart().concat(argumentName).concat(fastFormat.argumentEnd());
    }

    protected String generateArgumentName(SchematicInput<SENDER> input, Argument<?> argument) {
        return argument.getName();
    }

    protected boolean isVisible(SchematicInput<SENDER> input, CommandExecutor<SENDER> executor) {
        return validatorService.validate(input.getInvocation(), executor).isContinue();
    }

    protected <T> boolean isOptional(SchematicInput<SENDER> input, Argument<T> argument) {
        ParserSet<SENDER, T> parserSet = parserRegistry.getParserSet(argument.getType().getRawType(), argument.getKey());
        Parser<SENDER, T> parser = parserSet.getValidParser(argument);

        if (parser != null) {
            Range range = parser.getRange(argument);

            if (range.getMin() == 0) {
                return true;
            }
        }

        return argument.hasDefaultValue();
    }

}
