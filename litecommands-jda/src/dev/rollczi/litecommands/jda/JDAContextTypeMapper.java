package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.invocation.Invocation;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public interface JDAContextTypeMapper<T> {

    T map(Invocation<?> invocation, OptionMapping option);

}
