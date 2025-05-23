package dev.rollczi.litecommands.jda.visibility;

import dev.rollczi.litecommands.meta.MetaKey;

import dev.rollczi.litecommands.meta.MetaType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import net.dv8tion.jda.api.interactions.InteractionContextType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Visibility {

    MetaKey<List<InteractionContextType>> META_KEY = MetaKey.of("discord-visibility", MetaType.list(), List.of(
        InteractionContextType.GUILD,
        InteractionContextType.BOT_DM
    ));

    InteractionContextType[] value();

}
