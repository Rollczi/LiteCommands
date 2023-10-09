package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ParserRegistryImplTest {

    static final WrapFormat<String, String> STRING_FORMAT = WrapFormat.notWrapped(String.class);

    @Test
    void registerParser() {
        ParserRegistryImpl<TestSender> registry = new ParserRegistryImpl<>();

        registry.registerParser(String.class, ArgumentKey.of("custom"), new NamedResolver("custom"));
        registry.registerParser(String.class, ArgumentKey.of(), new NamedResolver("universal"));

        ParserSet<TestSender, String> custom = registry.getParserSet(String.class, ArgumentKey.of("custom"));
        ParserSet<TestSender, String> universal = registry.getParserSet(String.class, ArgumentKey.of());
        ParserSet<TestSender, String> missing = registry.getParserSet(String.class, ArgumentKey.of("missing"));

        Parser<TestSender, RawInput, String> customParser = assertOptional(custom.getParser(RawInput.class));
        Parser<TestSender, RawInput, String> universalParser = assertOptional(universal.getParser(RawInput.class));
        Parser<TestSender, RawInput, String> missingParser = assertOptional(missing.getParser(RawInput.class));

        assertThat(customParser.parse(null, Argument.of("custom", STRING_FORMAT), RawInput.of("in")))
            .isEqualTo(ParseResult.success("custom"));

        assertThat(universalParser.parse(null, Argument.of("universal", STRING_FORMAT), RawInput.of("in")))
            .isEqualTo(ParseResult.success("universal"));

        assertThat(missingParser.parse(null, Argument.of("missing", STRING_FORMAT), RawInput.of("in")))
            .isEqualTo(ParseResult.success("universal"));
    }

    private <T> T assertOptional(Optional<T> optional) {
        assertThat(optional).isPresent();
        return optional.get();
    }

    static class NamedResolver extends ArgumentResolver<TestSender, String> {
        private final String name;

        NamedResolver(String name) {
            this.name = name;
        }

        @Override
        protected ParseResult<String> parse(Invocation<TestSender> invocation, Argument<String> context, String argument) {
            return ParseResult.success(name);
        }
    }

}