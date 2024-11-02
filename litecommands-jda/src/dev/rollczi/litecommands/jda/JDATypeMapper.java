package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.invocation.Invocation;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public interface JDATypeMapper<T> extends JDAContextTypeMapper<T> {

    T map(OptionMapping option);

    @Override
    default T map(Invocation<?> event, OptionMapping option) {
        return map(option);
    }

}
