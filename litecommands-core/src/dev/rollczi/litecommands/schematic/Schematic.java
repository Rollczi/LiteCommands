package dev.rollczi.litecommands.schematic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Schematic {

    private final List<String> schematics = new ArrayList<>();

    public Schematic(Collection<String> schematics) {
        this.schematics.addAll(schematics);
    }

    public String join(CharSequence separator) {
        return String.join(separator, schematics);
    }

    public boolean isOnlyFirst() {
        return schematics.size() == 1;
    }

    public String first() {
        return schematics.get(0);
    }

    public List<String> all() {
        return Collections.unmodifiableList(schematics);
    }

}
