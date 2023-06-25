package dev.rollczi.litecommands.annotations.argument.arg;

import dev.rollczi.litecommands.annotations.argument.ParameterArgumentRequirementFactory;
import dev.rollczi.litecommands.annotations.command.requirement.RequirementAnnotation;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@RequirementAnnotation
public @interface Arg {

    String value() default "";

    class Factory<SENDER> extends ParameterArgumentRequirementFactory<Arg, SENDER> {

        public Factory(WrapperRegistry wrapperRegistry, ParserRegistry<SENDER> parserRegistry) {
            super(wrapperRegistry, parserRegistry, ArgArgument::new);
        }

    }

}
