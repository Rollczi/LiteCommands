package dev.rollczi.litecommands.schematic;

public class SchematicGeneratorReference<SENDER> implements SchematicGenerator<SENDER> {

    private SchematicGenerator<SENDER> schematicGenerator;

    public SchematicGeneratorReference(SchematicGenerator<SENDER> schematicGenerator) {
        this.schematicGenerator = schematicGenerator;
    }

    public void setSchematicGenerator(SchematicGenerator<SENDER> schematicGenerator) {
        this.schematicGenerator = schematicGenerator;
    }

    @Override
    public Schematic generate(SchematicInput<SENDER> schematicInput) {
        return schematicGenerator.generate(schematicInput);
    }

}
