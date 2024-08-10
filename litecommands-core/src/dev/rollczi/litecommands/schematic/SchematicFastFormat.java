package dev.rollczi.litecommands.schematic;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class SchematicFastFormat extends SchematicFormat {

    private final String prefix;
    private final String argumentStart;
    private final String argumentEnd;
    private final String optionalArgumentStart;
    private final String optionalArgumentEnd;

    public SchematicFastFormat(String prefix, String argumentStart, String argumentEnd, String optionalArgumentStart, String optionalArgumentEnd) {
        super(prefix, "", argumentStart + "%s" + argumentEnd, optionalArgumentStart + "%s" + optionalArgumentEnd);
        this.prefix = prefix;
        this.argumentStart = argumentStart;
        this.argumentEnd = argumentEnd;
        this.optionalArgumentStart = optionalArgumentStart;
        this.optionalArgumentEnd = optionalArgumentEnd;
    }

    public String prefix() {
        return this.prefix;
    }

    public String argumentStart() {
        return this.argumentStart;
    }

    public String argumentEnd() {
        return this.argumentEnd;
    }

    public String optionalArgumentStart() {
        return this.optionalArgumentStart;
    }

    public String optionalArgumentEnd() {
        return this.optionalArgumentEnd;
    }

}