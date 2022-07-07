package dev.rollczi.litecommands.command.section;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Section {

    String route();

    String[] aliases() default {};

    int required() default -1;

    FactoryAnnotationResolver<Section> RESOLVER = new SectionAnnotationResolver();

}
