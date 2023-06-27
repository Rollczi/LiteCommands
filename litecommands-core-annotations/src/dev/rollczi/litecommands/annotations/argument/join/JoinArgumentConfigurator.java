package dev.rollczi.litecommands.annotations.argument.join;

import dev.rollczi.litecommands.annotations.argument.ParameterArgumentRequirementFactory;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

public class JoinArgumentConfigurator<SENDER> extends ParameterArgumentRequirementFactory<Join, SENDER> {

    public JoinArgumentConfigurator(WrapperRegistry wrapperRegistry, ParserRegistry<SENDER> parserRegistry, SuggesterRegistry<SENDER> suggesterRegistry) {
        super(wrapperRegistry, parserRegistry, JoinArgument::new);
        parserRegistry.registerParser(String.class, Join.ARGUMENT_KEY, new JoinArgumentResolver<>());
        suggesterRegistry.registerSuggester(String.class, Join.ARGUMENT_KEY, new JoinArgumentResolver<>());
    }

}
