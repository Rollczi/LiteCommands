package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.platform.PlatformSettings;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.ApiStatus;

public class LiteJDASettings implements PlatformSettings {

    private JDACommandTranslator translator;

    JDACommandTranslator translator() {
        return translator;
    }

    @ApiStatus.Internal
    public LiteJDASettings translator(JDACommandTranslator translator) {
        this.translator = translator;
        return this;
    }

    public LiteJDASettings translator(UnaryOperator<JDACommandTranslator> config) {
        this.translator = config.apply(translator);
        return this;
    }

}
