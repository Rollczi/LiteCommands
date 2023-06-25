package dev.rollczi.litecommands.annotations.argument.join;

import dev.rollczi.litecommands.annotations.argument.ParameterArgumentRequirementFactory;
import dev.rollczi.litecommands.annotations.command.requirement.RequirementAnnotation;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@RequirementAnnotation
public @interface Join {

    String separator() default " ";

    int limit() default Integer.MAX_VALUE;

    ArgumentKey ARGUMENT_KEY = ArgumentKey.typed(JoinArgument.class);

    class Factory<SENDER> extends ParameterArgumentRequirementFactory<Join, SENDER> {

        public Factory(WrapperRegistry wrapperRegistry, ParserRegistry<SENDER> parserRegistry) {
            super(wrapperRegistry, parserRegistry, JoinArgument::new);
            parserRegistry.registerParser(String.class, ARGUMENT_KEY, new JoinArgumentResolver<>());
        }

    }

}
