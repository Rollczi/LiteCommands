package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;
import dev.rollczi.litecommands.builder.extension.LiteCommandsExtension;
import dev.rollczi.litecommands.platform.LiteSettings;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

public class LiteAdventureExtension<SENDER, C extends LiteSettings> implements LiteCommandsExtension<SENDER> {

    private final AdventureAudienceProvider<SENDER> adventureAudienceProvider;
    private boolean supportsMiniMessage = false;
    private boolean supportsLegacyColor = false;
    private ComponentSerializer<Component, ? extends Component, String> componentSerializer;
    private boolean colorizeArgument = false;

    public LiteAdventureExtension(AdventureAudienceProvider<SENDER> adventureAudienceProvider) {
        this.adventureAudienceProvider = adventureAudienceProvider;
    }

    public LiteAdventureExtension() {
        this.adventureAudienceProvider = AdventureAudienceProvider.simple();
    }

    public LiteAdventureExtension<SENDER, C> miniMessage(boolean supportsMiniMessage) {
        this.supportsMiniMessage = supportsMiniMessage;
        return this;
    }

    public LiteAdventureExtension<SENDER, C> legacyColor(boolean supportsLegacyColor) {
        this.supportsLegacyColor = supportsLegacyColor;
        return this;
    }

    public LiteAdventureExtension<SENDER, C> colorizeArgument(boolean colorizeArgument) {
        this.colorizeArgument = colorizeArgument;
        return this;
    }

    public LiteAdventureExtension<SENDER, C> serializer(ComponentSerializer<Component, ? extends Component, String> serializer) {
        this.componentSerializer = serializer;
        return this;
    }

    @Override
    public void extend(LiteCommandsBuilder<SENDER, C, ?> builder, LiteCommandsInternalBuilderApi<SENDER, ?> pattern) {
        if (componentSerializer == null) {
            componentSerializer = supportsMiniMessage
                ? AdventureMiniMessageFactory.produce(supportsLegacyColor)
                : PlainComponentSerializer.plain(supportsLegacyColor);
        }

        builder
            .argumentParser(Component.class, colorizeArgument ? new AdventureColoredComponentArgument<>(componentSerializer) : new AdventureComponentArgument<>())
            .argumentParser(Component.class, "raw", new AdventureComponentArgument<>())
            .argumentParser(Component.class, "color", new AdventureColoredComponentArgument<>(componentSerializer))

            .bindContext(Audience.class, new AdventureAudienceContextual<>(adventureAudienceProvider))

            .resultHandler(Component.class, new AdventureComponentHandler<>(adventureAudienceProvider))
            .resultHandler(String.class, new StringHandler<>(adventureAudienceProvider, componentSerializer)
            );
    }

}
