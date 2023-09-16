package dev.rollczi.litecommands.annotations.context;

import dev.rollczi.litecommands.annotations.command.requirement.ParameterRequirementFactory;
import dev.rollczi.litecommands.annotations.util.WrapperParameterUtil;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.command.requirement.RequirementResult;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.context.ContextRequirement;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Parameter;

public class ContextParameterRequirementFactory<SENDER> implements ParameterRequirementFactory<SENDER, Context> {

    private final ContextRegistry<SENDER> contextRegistry;
    private final WrapperRegistry wrapperRegistry;

    public ContextParameterRequirementFactory(ContextRegistry<SENDER> contextRegistry, WrapperRegistry wrapperRegistry) {
        this.contextRegistry = contextRegistry;
        this.wrapperRegistry = wrapperRegistry;
    }

    @Override
    public Requirement<SENDER, ?> create(Parameter parameter, Context annotation) {
        WrapFormat<?, ?> wrapFormat = WrapperParameterUtil.wrapperFormat(wrapperRegistry, parameter);

        return new ContextParameterPreparedArgument<>(parameter.getName(), wrapFormat, wrapperRegistry.getWrappedExpectedFactory(wrapFormat));
    }

    private class ContextParameterPreparedArgument<PARSED> implements ContextRequirement<SENDER, PARSED> {

        private final String name;
        private final WrapFormat<PARSED, ?> wrapFormat;
        private final Wrapper wrapper;
        private final Meta meta = Meta.create();

        private ContextParameterPreparedArgument(String name, WrapFormat<PARSED, ?> wrapFormat, Wrapper wrapper) {
            this.name = name;
            this.wrapFormat = wrapFormat;
            this.wrapper = wrapper;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public <CONTEXT extends ParseableInputMatcher<CONTEXT>> RequirementResult<PARSED> match(Invocation<SENDER> invocation, CONTEXT matcher) {
            ContextResult<PARSED> result = contextRegistry.provideContext(wrapFormat.getParsedType(), invocation);

            if (result.hasResult()) {
                return RequirementResult.success(wrapper.create(result.getResult(), wrapFormat));
            }

            return RequirementResult.failure(result.getError());
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

}
