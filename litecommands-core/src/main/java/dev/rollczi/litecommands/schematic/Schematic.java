package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.shared.Validation;

import java.util.Collections;
import java.util.List;

public class Schematic {

    private final List<String> schematics;

    public String first() {
        return schematics.get(0);
    }

    public boolean isOnlyFirst() {
        return schematics.size() == 1;
    }

    public Schematic(List<String> schematics) {
        Validation.isTrue(!schematics.isEmpty(), "Schematics must not be empty");

        this.schematics = schematics;
    }

    public List<String> getSchematics() {
        return Collections.unmodifiableList(schematics);
    }

}
