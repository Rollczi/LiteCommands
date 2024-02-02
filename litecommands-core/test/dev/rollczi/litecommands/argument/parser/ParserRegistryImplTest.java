package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeRange;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ParserRegistryImplTest {

    static final WrapFormat<String, String> STRING_FORMAT = WrapFormat.notWrapped(String.class);
    static final WrapFormat<Integer, Integer> INTEGER_FORMAT = WrapFormat.notWrapped(Integer.class);
    static final WrapFormat<Number, Number> NUMBER_FORMAT = WrapFormat.notWrapped(Number.class);

    @Test
    void registerParser() {
        ParserRegistryImpl<TestSender> registry = new ParserRegistryImpl<>();

        registry.registerParser(String.class, ArgumentKey.of(), new NamedResolver("universal"));

        ParserSet<TestSender, String> universal = registry.getParserSet(String.class, ArgumentKey.of());
        Parser<TestSender, String> universalParser = universal.getValidParserOrThrow(null, Argument.of("universal", STRING_FORMAT));

        assertThat(universalParser.parse(null, Argument.of("universal", STRING_FORMAT), RawInput.of("in")))
            .isEqualTo(ParseResult.success("universal"));
    }

    @Test
    void registerCustomParser() {
        ParserRegistryImpl<TestSender> registry = new ParserRegistryImpl<>();

        registry.registerParser(String.class, ArgumentKey.of("custom"), new NamedResolver("custom"));
        registry.registerParser(String.class, ArgumentKey.of(), new NamedResolver("universal"));

        ParserSet<TestSender, String> custom = registry.getParserSet(String.class, ArgumentKey.of("custom"));
        ParserSet<TestSender, String> universal = registry.getParserSet(String.class, ArgumentKey.of());
        ParserSet<TestSender, String> missing = registry.getParserSet(String.class, ArgumentKey.of("missing"));

        Parser<TestSender, String> customParser = assertNotNull(custom.getValidParser(null, Argument.of("custom", STRING_FORMAT)));
        Parser<TestSender, String> universalParser = assertNotNull(universal.getValidParser(null, Argument.of("universal", STRING_FORMAT)));
        Parser<TestSender, String> missingParser = assertNotNull(missing.getValidParser(null, Argument.of("missing", STRING_FORMAT)));

        assertThat(customParser.parse(null, Argument.of("custom", STRING_FORMAT), RawInput.of("in")))
            .isEqualTo(ParseResult.success("custom"));

        assertThat(universalParser.parse(null, Argument.of("universal", STRING_FORMAT), RawInput.of("in")))
            .isEqualTo(ParseResult.success("universal"));

        assertThat(missingParser.parse(null, Argument.of("missing", STRING_FORMAT), RawInput.of("in")))
            .isEqualTo(ParseResult.success("universal"));
    }

    @Test
    void testGenericTypes() {
        ParserRegistryImpl<TestSender> registry = new ParserRegistryImpl<>();
        registry.registerParser(TypeRange.upwards(Number.class), ArgumentKey.of(), new NumberResolver());

        ParserSet<TestSender, Number> number = registry.getParserSet(Number.class, ArgumentKey.of());
        ParserSet<TestSender, Integer> integer = registry.getParserSet(Integer.class, ArgumentKey.of());

        Parser<TestSender, Number> numberParser = assertNotNull(number.getValidParser(null, Argument.of("number", NUMBER_FORMAT)));
        Parser<TestSender, Integer> integerParser = assertNotNull(integer.getValidParser(null, Argument.of("integer", INTEGER_FORMAT)));

        assertThat(numberParser.parse(null, Argument.of("number", NUMBER_FORMAT), RawInput.of("1")))
            .isEqualTo(ParseResult.success(1));

        assertThat(integerParser.parse(null, Argument.of("integer", INTEGER_FORMAT), RawInput.of("1")))
            .isEqualTo(ParseResult.success(1));
    }

    private <T> T assertNotNull(T list) {
        assertThat(list).isNotNull();
        return list;
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

    static class NumberResolver extends ArgumentResolver<TestSender, Number> {
        @Override
        protected ParseResult<Number> parse(Invocation<TestSender> invocation, Argument<Number> context, String argument) {
            try {
                return ParseResult.success(Integer.parseInt(argument));
            }
            catch (NumberFormatException ingored) {
                return ParseResult.failure(InvalidUsage.Cause.INVALID_ARGUMENT);
            }
        }

    }

}