package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.validator.ValidatorService;

public interface SchematicGenerator<SENDER> {

    Schematic generate(SchematicInput<SENDER> schematicInput);

    static <SENDER> SchematicGenerator<SENDER> from(SchematicFormat format, ValidatorService<SENDER> validatorService) {
        return new SchematicGeneratorSimpleImpl<>(format, validatorService);
    }

}
