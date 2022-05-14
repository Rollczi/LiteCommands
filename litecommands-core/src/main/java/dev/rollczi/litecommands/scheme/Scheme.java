package dev.rollczi.litecommands.scheme;

import java.util.Collections;
import java.util.List;

public class Scheme {

    private final List<String> schemes;

    public Scheme(List<String> schemes) {
        this.schemes = schemes;
    }

    public List<String> getSchemes() {
        return Collections.unmodifiableList(schemes);
    }

}
