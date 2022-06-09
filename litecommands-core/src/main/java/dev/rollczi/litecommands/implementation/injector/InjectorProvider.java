package dev.rollczi.litecommands.implementation.injector;

import dev.rollczi.litecommands.injector.Injector;
import dev.rollczi.litecommands.injector.InjectorSettings;

public class InjectorProvider {

    private InjectorProvider() {
    }

    public static <SENDER> InjectorSettings<SENDER> settings() {
        return new LiteInjectorSettings<>();
    }

}
