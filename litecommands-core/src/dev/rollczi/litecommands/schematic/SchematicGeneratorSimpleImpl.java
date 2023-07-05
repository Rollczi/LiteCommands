package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.argument.ArgumentRequirement;
import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.validator.ValidatorService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SchematicGeneratorSimpleImpl<SENDER> implements SchematicGenerator<SENDER> {

    private static final String SEPARATOR = " ";

    private final SchematicFormat format;
    private final ValidatorService<SENDER> validatorService;

    public SchematicGeneratorSimpleImpl(SchematicFormat format, ValidatorService<SENDER> validatorService) {
        this.format = format;
        this.validatorService = validatorService;
    }

    @Override
    public Schematic generate(SchematicInput<SENDER> schematicInput) {
        List<String> schematics = generateRaw(schematicInput)
            .map(schematic -> format.prefix() + schematic.trim() + format.suffix())
            .collect(Collectors.toList());

        return new Schematic(schematics);
    }

    private Stream<String> generateRaw(SchematicInput<SENDER> schematicInput) {
        CommandExecutor<SENDER, ?> executor = schematicInput.getExecutor();
        String base = schematicInput.collectRoutes().stream()
            .map(route -> String.format(format.commandFormat(), route.getName()) + SEPARATOR)
            .collect(Collectors.joining());

        if (executor != null) {
            return Stream.of(base + generateExecutor(executor));
        }

        return generateRoute(base, schematicInput.getInvocation(), schematicInput.getLastRoute());
    }

    private Stream<String> generateRoute(String base, Invocation<SENDER> invocation, CommandRoute<SENDER> route) {
        Stream<String> children = route.getChildren().stream()
            .flatMap(subRoute -> generateRoute(base + subRoute.getName() + SEPARATOR, invocation, subRoute));

        Stream<String> executors = route.getExecutors().stream()
            .filter(executor -> validatorService.validate(invocation, executor).isContinue())
            .map(executor -> base + generateExecutor(executor));

        return Stream.concat(executors, children);
    }

    private String generateExecutor(CommandExecutor<SENDER, ?> executor) {
        return executor.getRequirements().stream()
            .filter(requirement -> requirement instanceof ArgumentRequirement)
            .map(requirement -> (ArgumentRequirement<?, ?>) requirement)
            .map(requirement -> String.format(requirement.isOptional() ? format.optionalArgumentFormat() : format.argumentFormat(), requirement.getArgument().getName()))
            .collect(Collectors.joining(SEPARATOR));
    }

}
