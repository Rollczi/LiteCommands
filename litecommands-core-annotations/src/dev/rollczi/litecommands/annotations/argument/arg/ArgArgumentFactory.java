package dev.rollczi.litecommands.annotations.argument.arg;

import dev.rollczi.litecommands.annotations.argument.ParameterArgumentRequirementFactory;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

public class ArgArgumentFactory<SENDER> extends ParameterArgumentRequirementFactory<Arg, SENDER> {

    public ArgArgumentFactory(WrapperRegistry wrapperRegistry, ParserRegistry<SENDER> parserRegistry) {
        super(wrapperRegistry, parserRegistry, ArgArgument::new);
    }

}
