package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.validator.ValidatorService;

public interface SchematicGenerator<SENDER> {

    Schematic generate(SchematicInput<SENDER> schematicInput);

    @Deprecated
    static <SENDER> SchematicGenerator<SENDER> from(SchematicFormat format, ValidatorService<SENDER> validatorService, ParserRegistry<SENDER> parserRegistry) {
        return new SimpleSchematicGenerator<>(format, validatorService, parserRegistry);
    }

}
