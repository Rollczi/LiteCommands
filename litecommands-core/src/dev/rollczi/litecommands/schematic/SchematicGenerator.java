package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.validator.ValidatorService;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

public interface SchematicGenerator<SENDER> {

    Schematic generate(SchematicInput<SENDER> schematicInput);

    static <SENDER> SchematicGenerator<SENDER> from(SchematicFormat format, ValidatorService<SENDER> validatorService, WrapperRegistry wrapperRegistry) {
        return new SchematicGeneratorSimpleImpl<>(format, validatorService, wrapperRegistry);
    }

}
