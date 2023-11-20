package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.configurator.LiteConfigurator;
import dev.rollczi.litecommands.extension.LiteExtension;
import dev.rollczi.litecommands.join.JoinArgument;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

public class LiteAdventureExtension<SENDER> implements LiteExtension<SENDER, LiteAdventureExtension.Settings> {

    private final AdventureAudienceProvider<SENDER> adventureAudienceProvider;
    private final Settings settings = new Settings();

    public LiteAdventureExtension(AdventureAudienceProvider<SENDER> adventureAudienceProvider) {
        this.adventureAudienceProvider = adventureAudienceProvider;
    }

    public LiteAdventureExtension() {
        this.adventureAudienceProvider = AdventureAudienceProvider.simple();
    }

    public static <T> LiteAdventureExtension<T> create() {
        return new LiteAdventureExtension<>();
    }


    @Override
    public void configure(LiteConfigurator<Settings> configurer) {
        configurer.configure(settings);
    }

    @Override
    public void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternal<SENDER, ?> internal) {
        if (settings.componentSerializer == null) {
            settings.componentSerializer = settings.supportsMiniMessage
                ? AdventureMiniMessageFactory.create(settings.supportsLegacyColor)
                : PlainComponentSerializerFactory.create(settings.supportsLegacyColor);
        }

        AdventureColoredComponentArgument<SENDER> colored = new AdventureColoredComponentArgument<>(settings.componentSerializer);
        AdventureComponentArgument<SENDER> raw = new AdventureComponentArgument<>();
        AdventureJoinComponentResolver<SENDER> joinColor = AdventureJoinComponentResolver.colored(settings.componentSerializer);
        AdventureJoinComponentResolver<SENDER> joinRaw = AdventureJoinComponentResolver.raw();

        builder
            .argument(Component.class, settings.colorizeArgument ? colored : raw)
            .argument(Component.class, ArgumentKey.of("raw"), raw)
            .argument(Component.class, ArgumentKey.of("color"), colored)

            .argumentParser(Component.class, JoinArgument.KEY, settings.colorizeArgument ? joinColor : joinRaw)
            .argumentParser(Component.class, JoinArgument.KEY.withKey("raw"), joinRaw)
            .argumentParser(Component.class, JoinArgument.KEY.withKey("color"), joinColor)

            .context(Audience.class, new AdventureAudienceContextual<>(adventureAudienceProvider))

            .result(Component.class, new AdventureComponentHandler<>(adventureAudienceProvider))
            .result(String.class, new StringHandler<>(adventureAudienceProvider, settings.componentSerializer))
            .bind(ComponentSerializer.class, () -> settings.componentSerializer)
            ;
    }

    public static class Settings {
        private boolean supportsMiniMessage = false;
        private boolean supportsLegacyColor = false;
        private ComponentSerializer<Component, ? extends Component, String> componentSerializer;
        private boolean colorizeArgument = false;

        public Settings miniMessage(boolean supportsMiniMessage) {
            this.supportsMiniMessage = supportsMiniMessage;
            return this;
        }

        public Settings legacyColor(boolean supportsLegacyColor) {
            this.supportsLegacyColor = supportsLegacyColor;
            return this;
        }

        public Settings colorizeArgument(boolean colorizeArgument) {
            this.colorizeArgument = colorizeArgument;
            return this;
        }

        public Settings serializer(ComponentSerializer<Component, ? extends Component, String> serializer) {
            this.componentSerializer = serializer;
            return this;
        }
    }

}
