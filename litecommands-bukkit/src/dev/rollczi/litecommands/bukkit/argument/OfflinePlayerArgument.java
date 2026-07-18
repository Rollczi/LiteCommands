package dev.rollczi.litecommands.bukkit.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;
import dev.rollczi.litecommands.reflect.ReflectUtil;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerType;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class OfflinePlayerArgument extends ArgumentResolver<CommandSender, OfflinePlayer> {

    private static final int SUGGESTION_LIMIT = 256;

    private final Server server;
    private final MessageRegistry<CommandSender> messageRegistry;
    private final boolean allowParseUnknownPlayers;
    private final Pattern playerNamePattern;
    private final ConcurrentSkipListSet<String> nicknames = new ConcurrentSkipListSet<>(String.CASE_INSENSITIVE_ORDER);

    public OfflinePlayerArgument(Server server, Plugin plugin, MessageRegistry<CommandSender> messageRegistry, boolean allowParseUnknownPlayers, Pattern playerNamePattern, Scheduler scheduler) {
        this.server = server;
        this.messageRegistry = messageRegistry;
        this.allowParseUnknownPlayers = allowParseUnknownPlayers;
        this.playerNamePattern = playerNamePattern;

        this.populateOfflinePlayers(scheduler);

        // Save new joining player names so our suggestions are more wide
        // TODO: Unregister this listener on Platform#unregister()
        server.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                nicknames.add(event.getPlayer().getName());
            }
        }, plugin);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected ParseResult<OfflinePlayer> parse(Invocation<CommandSender> invocation, Argument<OfflinePlayer> context, String argument) {
        return ParseResult.async(() -> {
            OfflinePlayer offlinePlayer = server.getOfflinePlayer(argument);
            long firstPlayed = offlinePlayer.getFirstPlayed();
            if (firstPlayed < 1 && !allowParseUnknownPlayers) {
                return ParseResult.failure(messageRegistry.get(LiteBukkitMessages.OFFLINE_PLAYER_NOT_FOUND, invocation, argument));
            }
            return ParseResult.success(offlinePlayer);
        });
    }

    @Override
    protected boolean match(Invocation<CommandSender> invocation, Argument<OfflinePlayer> context, String argument) {
        return playerNamePattern.matcher(argument).matches();
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<OfflinePlayer> argument, SuggestionContext context) {
        if (nicknames.size() < SUGGESTION_LIMIT) {
            return SuggestionResult.of(nicknames);
        }

        String input = context.getCurrent().multilevel();

        if (input.isEmpty()) {
            return nicknames.stream()
                .limit(SUGGESTION_LIMIT)
                .collect(SuggestionResult.collector());
        }

        return nicknames.subSet(input, input + Character.MAX_VALUE).stream()
            .limit(SUGGESTION_LIMIT)
            .collect(SuggestionResult.collector());
    }

    private void populateOfflinePlayers(Scheduler scheduler) {

        // We want to avoid calling Server#getOfflinePlayers() on the main thread, as it's blocking
        try {
            Object playerDataStorage = getPlayerStorage(server);
            if (playerDataStorage != null) {
                // playerDataStorage.getPlayerDir()
                File storageDir = ReflectUtil.getFromMethod(playerDataStorage, "getPlayerDir");

                // mimic the getOfflinePlayers() behavior we care about on an executor thread
                scheduler.run(SchedulerType.EXECUTOR, () -> {
                    // use a map here so we can dedupe ids
                    Map<UUID, String> nicknames = new HashMap<>();

                    // iterate over directory async & extract usernames
                    File[] files = storageDir.listFiles((dir, name) -> name.endsWith(".dat"));

                    if (files != null) {
                        for (File file : files) {
                            retrievePlayerName(file, nicknames);
                        }
                    }

                    // combine with online player names
                    scheduler.run(SchedulerType.MAIN, () -> {
                        server.getOnlinePlayers()
                            .forEach(player -> nicknames.put(player.getUniqueId(), player.getName()));

                        // publish
                        this.nicknames.addAll(nicknames.values());
                    });
                });
                return;
            }
        }
        catch (Throwable ignored) {
            // the server has altered OfflinePlayer logic - fallthrough
        }

        // Server#getOfflinePlayers() is not thread-safe
        scheduler.run(SchedulerType.MAIN, () -> {
            for (OfflinePlayer player : server.getOfflinePlayers()) {
                String name = player.getName();
                // According to Bukkit API documentation, offline player objects may have no name
                //  if the server only knows about their unique ID when requested,
                //  so this check could prevent NullPointerException in this case
                if (name != null) {
                    nicknames.add(name);
                }
            }
        });
    }

    private static void retrievePlayerName(File file, Map<UUID, String> nicknames) {
        String fileName = file.getName();
        try {
            UUID id = UUID.fromString(fileName.substring(0, fileName.length() - 4));
            String name = readLastKnownName(file);

            if (name != null && !name.isEmpty()) {
                nicknames.put(id, name);
            }
        }
        catch (Throwable ignored) {}
    }

    // NBT tag type ids, see https://minecraft.wiki/w/NBT_format
    private static final byte TAG_END = 0;
    private static final byte TAG_BYTE = 1;
    private static final byte TAG_SHORT = 2;
    private static final byte TAG_INT = 3;
    private static final byte TAG_LONG = 4;
    private static final byte TAG_FLOAT = 5;
    private static final byte TAG_DOUBLE = 6;
    private static final byte TAG_BYTE_ARRAY = 7;
    private static final byte TAG_STRING = 8;
    private static final byte TAG_LIST = 9;
    private static final byte TAG_COMPOUND = 10;
    private static final byte TAG_INT_ARRAY = 11;
    private static final byte TAG_LONG_ARRAY = 12;

    private static @Nullable String readLastKnownName(File dataFile) {
        try (DataInputStream in = new DataInputStream(
            new BufferedInputStream(new GZIPInputStream(new FileInputStream(dataFile))))) {
            if (in.readByte() != TAG_COMPOUND) {
                return null; // the  root tag is always a compound
            }
            in.readUTF(); // root name, usually empty
            return searchCompound(in);
        }
        catch (Exception ignored) {
            return null;
        }
    }

    private static @Nullable String searchCompound(DataInputStream in) throws IOException {
        byte type;
        while ((type = in.readByte()) != TAG_END) {
            String name = in.readUTF();

            if (type == TAG_STRING) {
                String value = in.readUTF();
                if (name.equals("lastKnownName")) {
                    return value;
                }
            }
            else {
                String found = skip(in, type);
                if (found != null) {
                    return found;
                }
            }
        }

        return null;
    }

    private static @Nullable String skip(DataInputStream in, byte type) throws IOException {
        switch (type) {
            case TAG_BYTE: in.readByte(); return null;
            case TAG_SHORT: in.readShort(); return null;
            case TAG_INT: in.readInt(); return null;
            case TAG_LONG: in.readLong(); return null;
            case TAG_FLOAT: in.readFloat(); return null;
            case TAG_DOUBLE: in.readDouble(); return null;
            case TAG_STRING: in.readUTF(); return null;
            case TAG_BYTE_ARRAY: in.skipBytes(in.readInt()); return null;
            case TAG_INT_ARRAY: in.skipBytes(in.readInt() * 4); return null;
            case TAG_LONG_ARRAY: in.skipBytes(in.readInt() * 8); return null;
            case TAG_COMPOUND: return searchCompound(in);
            case TAG_LIST: {
                byte elementType = in.readByte();
                int length = in.readInt();
                for (int i = 0; i < length; i++) {
                    String found = skip(in, elementType);
                    if (found != null) {
                        return found;
                    }
                }
                return null;
            }
            default:
                throw new IOException("Unknown NBT tag type: " + type);
        }
    }

    private static @Nullable Object getPlayerStorage(Server server) throws LiteCommandsReflectException {

        Object console = ReflectUtil.getFromField(server, "console");

        Object dataStorage = null;

        try {
            // console.playerDataStorage

            dataStorage = ReflectUtil.getFromField(console, "playerDataStorage");
        }
        catch (Exception ignored) {}

        // fallback for legacy versions
        if (dataStorage == null) {
            // console.worlds.get(0).getDataManager()

            //noinspection rawtypes
            List worlds = ReflectUtil.getFromField(console, "worlds");
            Object world = worlds.get(0);
            dataStorage = ReflectUtil.getFromMethod(world, "getDataManager");
        }

        return dataStorage;
    }
}
