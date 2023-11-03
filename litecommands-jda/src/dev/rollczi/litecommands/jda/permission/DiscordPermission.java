package dev.rollczi.litecommands.jda.permission;

import dev.rollczi.litecommands.meta.MetaKey;
import dev.rollczi.litecommands.meta.MetaType;
import net.dv8tion.jda.api.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface DiscordPermission {

    MetaKey<List<Permission>> META_KEY = MetaKey.of("discord-permission", MetaType.list(), Collections.emptyList());

    Permission[] value();

}
