package dev.rollczi.litecommands.jda.visibility;

import dev.rollczi.litecommands.meta.MetaKey;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Visibility {

    MetaKey<VisibilityScope> META_KEY = MetaKey.of("discord-visibility", VisibilityScope.class, VisibilityScope.GUILD);

    VisibilityScope value() default VisibilityScope.GUILD;

}
