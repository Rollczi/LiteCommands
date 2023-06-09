package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;
import dev.rollczi.litecommands.builder.extension.LiteCommandsExtension;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

public class LiteAdventureExtension<SENDER> implements LiteCommandsExtension<SENDER> {

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

    public static <T> LiteAdventureExtension<T> create() {
        return new LiteAdventureExtension<>();
    }

    public LiteAdventureExtension<SENDER> miniMessage(boolean supportsMiniMessage) {
        this.supportsMiniMessage = supportsMiniMessage;
        return this;
    }

    public LiteAdventureExtension<SENDER> legacyColor(boolean supportsLegacyColor) {
        this.supportsLegacyColor = supportsLegacyColor;
        return this;
    }

    public LiteAdventureExtension<SENDER> colorizeArgument(boolean colorizeArgument) {
        this.colorizeArgument = colorizeArgument;
        return this;
    }

    public LiteAdventureExtension<SENDER> serializer(ComponentSerializer<Component, ? extends Component, String> serializer) {
        this.componentSerializer = serializer;
        return this;
    }

    @Override
    public void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternalBuilderApi<SENDER, ?> pattern) {
        if (componentSerializer == null) {
            componentSerializer = supportsMiniMessage
                ? AdventureMiniMessageFactory.produce(supportsLegacyColor)
                : PlainComponentSerializer.plain(supportsLegacyColor);
        }

        builder
            .argument(Component.class, colorizeArgument ? new AdventureColoredComponentArgument<>(componentSerializer) : new AdventureComponentArgument<>())
            .argument(Component.class, "raw", new AdventureComponentArgument<>())
            .argument(Component.class, "color", new AdventureColoredComponentArgument<>(componentSerializer))

            .context(Audience.class, new AdventureAudienceContextual<>(adventureAudienceProvider))

            .result(Component.class, new AdventureComponentHandler<>(adventureAudienceProvider))
            .result(String.class, new StringHandler<>(adventureAudienceProvider, componentSerializer))
            ;
    }
}
