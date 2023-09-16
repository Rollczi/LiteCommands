package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.argument.parser.RawInputParser;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.requirement.ArgumentRequirement;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.command.executor.AbstractCommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.requirement.RequirementResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.wrapper.Wrap;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.std.ValueWrapper;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
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
    public CommandExecutorMatchResult match(RequirementsResult<SENDER> requirementsResult) {
        return CommandExecutorMatchResult.success(() -> CommandExecuteResult.success(this, this.result));
    }

    private class TestArgumentRequirement<PARSED> implements ArgumentRequirement<SENDER, PARSED> {

        private final Argument<PARSED> argument;
        private final Wrapper wrapper;
        private final ParserSet<SENDER, PARSED> parserSet;
        private final Meta meta = Meta.create();

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
        public boolean isWrapperOptional() {
            return wrapper.canCreateEmpty();
        }

        @Override
        public String getName() {
            return argument.getName();
        }

        @Override
        public AnnotationHolder<?, PARSED, ?> getAnnotationHolder() {
            return argument.getAnnotationHolder();
        }

        @Override
        public <MATCHER extends ParseableInputMatcher<MATCHER>> RequirementResult<PARSED> match(Invocation<SENDER> invocation, MATCHER matcher) {
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

        @Override
        public Meta meta() {
            return meta;
        }

        @Override
        public @Nullable MetaHolder parentMeta() {
            return null;
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

        @Override
        public AnnotationHolder<?, PARSED, ?> getAnnotationHolder() {
            Arg arg = new Arg() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return Arg.class;
                }

                @Override
                public String value() {
                    return name;
                }
            };

            return AnnotationHolder.of(arg, wrapFormat, () -> name);
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
