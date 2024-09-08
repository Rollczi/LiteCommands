package dev.rollczi.litecommands.jda;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public interface JDATypeMapper<T> {
    T map(OptionMapping option);
}
