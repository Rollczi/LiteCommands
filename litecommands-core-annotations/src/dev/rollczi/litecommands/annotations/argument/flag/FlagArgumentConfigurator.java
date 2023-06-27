package dev.rollczi.litecommands.annotations.argument.flag;

import dev.rollczi.litecommands.annotations.argument.ParameterArgumentRequirementFactory;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

public class FlagArgumentConfigurator<SENDER> extends ParameterArgumentRequirementFactory<Flag, SENDER> {

    public FlagArgumentConfigurator(WrapperRegistry wrapperRegistry, ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(wrapperRegistry, parserRegistry, FlagArgument::new);
        FlagArgumentResolver<SENDER> resolver = new FlagArgumentResolver<>();

        parserRegistry.registerParser(boolean.class, Flag.ARGUMENT_KEY, resolver);
        parserRegistry.registerParser(Boolean.class, Flag.ARGUMENT_KEY, resolver);
        suggesterRegistry.registerSuggester(boolean.class, Flag.ARGUMENT_KEY, resolver);
        suggesterRegistry.registerSuggester(Boolean.class, Flag.ARGUMENT_KEY, resolver);
    }

}
