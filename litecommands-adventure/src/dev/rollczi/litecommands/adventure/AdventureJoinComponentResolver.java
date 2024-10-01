package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.join.JoinProfile;
import dev.rollczi.litecommands.join.JoinArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.serializer.ComponentSerializer;

import java.util.List;

class AdventureJoinComponentResolver<SENDER> extends JoinArgumentResolver<SENDER, Component> {

    private final ComponentSerializer<Component, ?, String> serializer;

    private AdventureJoinComponentResolver(ComponentSerializer<Component, ?, String> serializer) {
        this.serializer = serializer;
    }

    @Override
    protected Component join(JoinProfile argument, List<String> values) {
        JoinConfiguration configuration = JoinConfiguration
            .separator(serializer.deserialize(argument.getSeparator()));

        Component[] components = values.stream()
            .map(rawValue -> serializer.deserialize(rawValue))
            .toArray(length -> new Component[length]);

        return Component.join(configuration, components);
    }

    public static <SENDER> AdventureJoinComponentResolver<SENDER> colored(ComponentSerializer<Component, ?, String> kyoriComponentSerializer) {
        return new AdventureJoinComponentResolver<>(kyoriComponentSerializer);
    }

    public static <SENDER> AdventureJoinComponentResolver<SENDER> raw() {
        return new AdventureJoinComponentResolver<>(PlainComponentSerializerFactory.create(false));
    }

}