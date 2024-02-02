package dev.rollczi.litecommands.minestom.test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

public class TestPlayerConnection extends PlayerConnection {

    @Override
    public void sendPacket(@NotNull SendablePacket packet) {
    }

    @Override
    public @NotNull SocketAddress getRemoteAddress() {
        return new InetSocketAddress(0);
    }

}
