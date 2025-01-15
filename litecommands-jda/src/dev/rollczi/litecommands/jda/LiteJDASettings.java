package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.shared.Lazy;
import dev.rollczi.litecommands.shared.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.ApiStatus;

public class LiteJDASettings implements PlatformSettings {

    private JDACommandTranslator translator;
    private final List<Lazy<Long>> predefinedGuilds = new ArrayList<>();

    JDACommandTranslator translator() {
        return translator;
    }

    @ApiStatus.Internal
    public LiteJDASettings translator(JDACommandTranslator translator) {
        this.translator = translator;
        return this;
    }

    public LiteJDASettings translator(UnaryOperator<JDACommandTranslator> config) {
        this.translator = config.apply(translator);
        return this;
    }

    public LiteJDASettings guilds(Guild... guilds) {
        for (Guild guild : guilds) {
            predefinedGuilds.add(new Lazy<>(() -> guild.getIdLong()));
        }
        return this;
    }

    public LiteJDASettings guilds(String... guildIds) {
        for (String guildId : guildIds) {
            predefinedGuilds.add(new Lazy<>(() -> tryToParse(guildId)));
        }
        return this;
    }

    private static long tryToParse(String guildId) {
        try {
            return Long.parseLong(guildId);
        } catch (NumberFormatException numberFormatException) {
            throw new IllegalArgumentException("Cannot parse guild id: " + guildId);
        }
    }

    public LiteJDASettings guilds(long... guildIds) {
        for (long guildId : guildIds) {
            predefinedGuilds.add(new Lazy<>(() -> guildId));
        }
        return this;
    }

    List<Guild> getGuilds(JDA jda) {
        return predefinedGuilds.stream()
            .map(longLazy -> longLazy.get())
            .map(id -> findGuild(jda, id))
            .collect(Collectors.toList());
    }

    private static Guild findGuild(JDA jda, Long id) {
        Guild guildById = jda.getGuildById(id);
        Preconditions.checkArgument(guildById != null, "Cannot find guild by id: " + id);
        return guildById;
    }

}
