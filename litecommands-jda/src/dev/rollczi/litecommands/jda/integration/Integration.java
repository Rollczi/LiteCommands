package dev.rollczi.litecommands.jda.integration;

import dev.rollczi.litecommands.meta.MetaKey;
import dev.rollczi.litecommands.meta.MetaType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.InteractionType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Integration {

    MetaKey<List<IntegrationType>> META_KEY = MetaKey.of("discord-integration", MetaType.list(), List.of(IntegrationType.GUILD_INSTALL));

    IntegrationType[] value();

}
