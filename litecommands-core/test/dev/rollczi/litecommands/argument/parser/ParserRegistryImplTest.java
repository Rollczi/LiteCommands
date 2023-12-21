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
import java.util.List;
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
        Parser<TestSender, RawInput, String> universalParser = assertOne(universal.getParsers(RawInput.class));

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

        Parser<TestSender, RawInput, String> customParser = assertOne(custom.getParsers(RawInput.class));
        Parser<TestSender, RawInput, String> universalParser = assertOne(universal.getParsers(RawInput.class));
        Parser<TestSender, RawInput, String> missingParser = assertOne(missing.getParsers(RawInput.class));

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

        Parser<TestSender, RawInput, Number> numberParser = assertOne(number.getParsers(RawInput.class));
        Parser<TestSender, RawInput, Integer> integerParser = assertOne(integer.getParsers(RawInput.class));

        assertThat(numberParser.parse(null, Argument.of("number", NUMBER_FORMAT), RawInput.of("1")))
            .isEqualTo(ParseResult.success(1));

        assertThat(integerParser.parse(null, Argument.of("integer", INTEGER_FORMAT), RawInput.of("1")))
            .isEqualTo(ParseResult.success(1));
    }

    private <T> T assertOne(List<T> list) {
        assertThat(list).hasSize(1);
        return list.get(0);
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