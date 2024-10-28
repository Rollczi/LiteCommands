package dev.rollczi.litecommands.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;
import dev.rollczi.litecommands.reflect.ReflectUtil;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.libs.jline.console.completer.Completer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

class TabCompleteProtocolLibAsync extends TabCompleteSync {

    private final static ProtocolManager MANAGER = ProtocolLibrary.getProtocolManager();

    private final Scheduler scheduler;
    private PacketAdapter listener;

    TabCompleteProtocolLibAsync(Plugin plugin, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.tryReplaceConsoleTabCompleter(plugin.getServer());
        MANAGER.addPacketListener(listener = new PacketAdapter(plugin, PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                handlePacket(event);
            }
        });

    }

    private void tryReplaceConsoleTabCompleter(Server server) {
        Object craftServer = ReflectUtil.getFromMethod(server, "getHandle");
        Object minecraftServer = getMinecraftServer(craftServer);
        ConsoleReader reader = ReflectUtil.getFromField(minecraftServer, "reader");

        Collection<Completer> completers = reader.getCompleters();

        if (completers.size() == 1) {
            Completer completer = completers.iterator().next();

            reader.removeCompleter(completer);
            reader.addCompleter(new ProtocolLibConsoleTabConsoleCompleter(server.getConsoleSender(), completer));
        }
    }

    private Object getMinecraftServer(Object craftServer) {
        try {
            return ReflectUtil.getFromField(craftServer, "server");
        }
        catch (LiteCommandsReflectException exception) {
            return ReflectUtil.getFromField(craftServer, "cserver");
        }
    }

    private void handlePacket(PacketEvent event) {
        Player player = event.getPlayer();
        String buffer = event.getPacket().getStrings().read(0);

        if (!buffer.startsWith(RawCommand.COMMAND_SLASH)) {
            return;
        }

        RawCommand rawCommand = RawCommand.from(buffer);
        String commandName = rawCommand.getLabel();
        BukkitCommand command = listeners.get(commandName);

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
                List<String> list = suggestions.stream().map(c -> c.multilevel()).collect(Collectors.toList());

                PacketContainer packet = MANAGER.createPacket(PacketType.Play.Server.TAB_COMPLETE);
                packet.getStringArrays().write(0, list.toArray(new String[0]));

                MANAGER.sendServerPacket(player, packet);
            }
            catch (InterruptedException | ExecutionException | TimeoutException e) {
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

    private class ProtocolLibConsoleTabConsoleCompleter implements Completer {

        private final Completer completer;
        private final CommandSender consoleSender;

        public ProtocolLibConsoleTabConsoleCompleter(CommandSender consoleSender, Completer completer) {
            this.completer = completer;
            this.consoleSender = consoleSender;
        }

        @Override
        public int complete(String buffer, int cursor, List<CharSequence> candidates) {
            int completed = completer.complete(buffer, cursor, candidates);
            Set<Suggestion> result = callListener(consoleSender, buffer);

            if (result == null && cursor == completed) {
                return completed;
            }

            if (result != null) {
                for (Suggestion suggestion : result) {
                    candidates.add(suggestion.multilevel());
                }
            }

            int lastSpace = buffer.lastIndexOf(' ');

            if (lastSpace == -1) {
                return cursor - buffer.length();
            } else {
                return cursor - (buffer.length() - lastSpace - 1);
            }
        }

    }
}
