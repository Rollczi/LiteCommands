package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.argument.parser.RawInputParser;
import dev.rollczi.litecommands.argument.parser.input.ParsableInputMatcher;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.command.requirement.RequirementMatch;
import dev.rollczi.litecommands.argument.ArgumentRequirement;
import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.command.executor.AbstractCommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.command.requirement.RequirementResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.wrapper.Wrap;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.std.ValueWrapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class TestExecutor<SENDER> extends AbstractCommandExecutor<SENDER, Requirement<SENDER, ?>> {

    private final Object result;

    public TestExecutor(CommandRoute<SENDER> parent, Object result) {
        super(parent, Collections.emptyList());
        this.result = result;
    }

    public TestExecutor(CommandRoute<SENDER> parent) {
        super(parent, Collections.emptyList());
        this.result = null;
    }

    public TestExecutor<SENDER> withStringArg(String name) {
        return withArg(name, String.class, (invocation, input) -> ParseResult.success(input));
    }

    @SuppressWarnings("unchecked")
    public <T> TestExecutor<SENDER> withArg(String name, Class<T> type, BiFunction<Invocation<SENDER>, String, ParseResult<T>> parser) {
        requirements.add(new TestArgumentRequirement<>(new TestArgument<>(name, type), new ValueWrapper(), new ParserSet<SENDER, T>() {
            @Override
            public <INPUT> Optional<Parser<SENDER, INPUT, T>> getParser(Class<INPUT> inType) {
                if (inType != RawInput.class) {
                    return Optional.empty();
                }

                return Optional.of((Parser<SENDER, INPUT, T>) new SimpleRawInputParser<>(parser));
            }

            @Override
            public Collection<Parser<SENDER, ?, T>> getParsers() {
                return Collections.emptyList();
            }
        }));

        return this;
    }

    @Override
    public CommandExecutorMatchResult match(List<RequirementMatch<SENDER, Requirement<SENDER, ?>, Object>> results) {
        return CommandExecutorMatchResult.success(() -> CommandExecuteResult.success(this, result));
    }

    private class TestArgumentRequirement<PARSED> implements ArgumentRequirement<SENDER, PARSED> {

        private final Argument<PARSED> argument;
        private final Wrapper wrapper;
        private final ParserSet<SENDER, PARSED> parserSet;

        public TestArgumentRequirement(Argument<PARSED> argument, Wrapper wrapper, ParserSet<SENDER, PARSED> parserSet) {
            this.argument = argument;
            this.wrapper = wrapper;
            this.parserSet = parserSet;
        }

        @Override
        public Argument<PARSED> getArgument() {
            return this.argument;
        }

        @Override
        public boolean isOptional() {
            return wrapper.canCreateEmpty();
        }

        @Override
        public <MATCHER extends ParsableInputMatcher<MATCHER>> RequirementResult<PARSED> match(Invocation<SENDER> invocation, MATCHER matcher) {
            ParseResult<PARSED> matchArgument = matcher.nextArgument(invocation, argument, parserSet);

            if (matchArgument.isFailed()) {
                return RequirementResult.failure(matchArgument.getFailedReason());
            }

            return RequirementResult.success(new Wrap<PARSED>() {
                @Override
                public Object unwrap() {
                    return matchArgument.getSuccessfulResult();
                }

                @Override
                public Class<PARSED> getExpectedType() {
                    return argument.getWrapperFormat().getParsedType();
                }
            });
        }
    }

    private static class TestArgument<PARSED> implements Argument<PARSED> {

        private final String name;
        private final WrapFormat<PARSED, ?> wrapFormat;

        private TestArgument(String name, Class<PARSED> type) {
            this.name = name;
            this.wrapFormat = WrapFormat.notWrapped(type);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public WrapFormat<PARSED, ?> getWrapperFormat() {
            return this.wrapFormat;
        }
    }

    private class SimpleRawInputParser<T> implements RawInputParser<SENDER, T> {
        private final BiFunction<Invocation<SENDER>, String, ParseResult<T>> parser;

        public SimpleRawInputParser(BiFunction<Invocation<SENDER>, String, ParseResult<T>> parser) {
            this.parser = parser;
        }

        @Override
        public Range getRange(Argument<T> argument) {
            return Range.of(1);
        }

        @Override
        public ParseResult<T> parse(Invocation<SENDER> invocation, Argument<T> argument, RawInput input) {
            return parser.apply(invocation, input.next());
        }
    }
}
