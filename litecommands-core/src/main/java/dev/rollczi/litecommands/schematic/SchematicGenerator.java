package dev.rollczi.litecommands.schematic;

import java.util.List;

public interface SchematicGenerator {

    Schematic generateSchematic(SchematicContext<?> result, SchematicFormat schematicFormat);

    List<String> generate(SchematicContext<?> result, SchematicFormat schematicFormat);

    static SchematicGenerator simple() {
        return new SimpleSchematicGenerator();
    }

}
