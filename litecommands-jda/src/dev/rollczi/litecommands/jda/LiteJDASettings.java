package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.platform.PlatformSettings;

public class LiteJDASettings implements PlatformSettings {

    private JDACommandTranslator translator;

    JDACommandTranslator translator() {
        return translator;
    }

    LiteJDASettings translator(JDACommandTranslator translator) {
        this.translator = translator;
        return this;
    }

}
