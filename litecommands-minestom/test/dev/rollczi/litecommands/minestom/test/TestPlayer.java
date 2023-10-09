package dev.rollczi.litecommands.minestom.test;

import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

public class TestPlayer extends Player {

    public TestPlayer(@NotNull UUID uuid, @NotNull String username) {
        super(uuid, username, new TestPlayerConnection());
    }

    private static class TestPlayerConnection extends PlayerConnection {

        @Override
        public void sendPacket(@NotNull SendablePacket packet) {}

        @Override
        public @NotNull SocketAddress getRemoteAddress() {
            return new InetSocketAddress(0);
        }

    }
}
