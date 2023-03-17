package dev.rollczi.litecommands.modern.schematic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Schematic {

    private final List<String> schematics = new ArrayList<>();

    Schematic(Collection<String> schematics) {
        this.schematics.addAll(schematics);
    }

    public List<String> getSchematics() {
        return Collections.unmodifiableList(schematics);
    }

}
