package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.extension.LiteExtension;
import dev.rollczi.litecommands.join.JoinArgument;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

public class LiteAdventureExtension<SENDER> implements LiteExtension<SENDER> {

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
    public void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternal<SENDER, ?> internal) {
        if (componentSerializer == null) {
            componentSerializer = supportsMiniMessage
                ? AdventureMiniMessageFactory.create(supportsLegacyColor)
                : PlainComponentSerializerFactory.create(supportsLegacyColor);
        }

        AdventureColoredComponentArgument<SENDER> colored = new AdventureColoredComponentArgument<>(componentSerializer);
        AdventureComponentArgument<SENDER> raw = new AdventureComponentArgument<>();
        AdventureJoinComponentResolver<SENDER> joinColor = AdventureJoinComponentResolver.colored(componentSerializer);
        AdventureJoinComponentResolver<SENDER> joinRaw = AdventureJoinComponentResolver.raw();

        builder
            .argument(Component.class, colorizeArgument ? colored : raw)
            .argument(Component.class, ArgumentKey.of("raw"), raw)
            .argument(Component.class, ArgumentKey.of("color"), colored)

            .argumentParser(Component.class, JoinArgument.KEY, colorizeArgument ? joinColor : joinRaw)
            .argumentParser(Component.class, JoinArgument.KEY.withKey("raw"), joinRaw)
            .argumentParser(Component.class, JoinArgument.KEY.withKey("color"), joinColor)

            .context(Audience.class, new AdventureAudienceContextual<>(adventureAudienceProvider))

            .result(Component.class, new AdventureComponentHandler<>(adventureAudienceProvider))
            .result(String.class, new StringHandler<>(adventureAudienceProvider, componentSerializer))
            .bind(ComponentSerializer.class, () -> componentSerializer)
            ;
    }
}
