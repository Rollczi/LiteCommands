package dev.rollczi.litecommands.minestom.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

public class TestPlayer extends Player {

    private final List<Object> messages = new ArrayList<>();

    public TestPlayer(@NotNull PlayerConnection connection, GameProfile profile) {
        super(connection, profile);
    }

    @Override
    public CompletableFuture<Void> UNSAFE_init() {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
        messages.add(componentToString(message));
    }

    public List<Object> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    private String componentToString(Component component) {
        StringBuilder builder = new StringBuilder();

        if (component instanceof TextComponent) {
            builder.append(((TextComponent) component).content());
        }

        for (Component child : component.children()) {
            builder.append(componentToString(child));
        }

        return builder.toString();
    }

}
