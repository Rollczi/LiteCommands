package dev.rollczi.litecommands.folia;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class TabCompleteProtocolLibAsync extends TabCompleteSync {

    private final static ProtocolManager MANAGER = ProtocolLibrary.getProtocolManager();

    private final Scheduler scheduler;
    private PacketAdapter listener;

    TabCompleteProtocolLibAsync(Plugin plugin, Scheduler scheduler) {
        this.scheduler = scheduler;
        MANAGER.addPacketListener(listener = new PacketAdapter(plugin, PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                handlePacket(event);
            }
        });

    }

    private void handlePacket(PacketEvent event) {
        Player player = event.getPlayer();
        String buffer = event.getPacket().getStrings().read(0);

        if (!buffer.startsWith(RawCommand.COMMAND_SLASH)) {
            return;
        }

        RawCommand rawCommand = RawCommand.from(buffer);
        String commandName = rawCommand.getLabel();
        FoliaCommand command = listeners.get(commandName);

        if (command == null) {
            return;
        }

        event.setCancelled(true);
        scheduler.run(SchedulerPoll.SUGGESTER, () -> {
            try {
                Set<Suggestion> suggestions = command.suggest(player, commandName, rawCommand.getArgs().toArray(new String[0]))
                    .get(15, TimeUnit.SECONDS);

                if (suggestions == null) {
                    return;
                }
                String[] list = suggestions.stream().map(c -> c.multilevel())
                    .toArray(String[]::new);

                PacketContainer packet = MANAGER.createPacket(PacketType.Play.Server.TAB_COMPLETE);
                packet.getStringArrays().write(0, list);

                MANAGER.sendServerPacket(player, packet);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void close() {
        super.close();

        if (listener != null) {
            ProtocolLibrary.getProtocolManager().removePacketListener(listener);
            listener = null;
        }
    }

}
