package dev.rollczi.litecommands.schematic;

public interface SchematicGenerator<SENDER> {

    Schematic generate(SchematicInput<SENDER> schematicInput);

}
