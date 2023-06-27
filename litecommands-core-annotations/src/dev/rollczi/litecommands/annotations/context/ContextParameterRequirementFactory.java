package dev.rollczi.litecommands.annotations.context;

import dev.rollczi.litecommands.annotations.command.requirement.ParameterRequirement;
import dev.rollczi.litecommands.annotations.command.requirement.ParameterRequirementFactory;
import dev.rollczi.litecommands.annotations.util.WrapperParameterUtil;
import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.command.requirement.RequirementResult;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.context.ContextRequirement;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class ContextParameterRequirementFactory<SENDER> implements ParameterRequirementFactory<SENDER, Context> {

    private final ContextRegistry<SENDER> contextRegistry;
    private final WrapperRegistry wrapperRegistry;

    public ContextParameterRequirementFactory(ContextRegistry<SENDER> contextRegistry, WrapperRegistry wrapperRegistry) {
        this.contextRegistry = contextRegistry;
        this.wrapperRegistry = wrapperRegistry;
    }

    @Override
    public ParameterRequirement<SENDER, ?> create(Parameter parameter, Context annotation) {
        Method method = (Method) parameter.getDeclaringExecutable();
        int index = Arrays.asList(method.getParameters()).indexOf(parameter);
        WrapFormat<?, ?> wrapFormat = WrapperParameterUtil.wrapperFormat(wrapperRegistry, parameter);

        return new ContextParameterPreparedArgument<>(parameter, index, wrapFormat, wrapperRegistry.getWrappedExpectedFactory(wrapFormat));
    }

    private class ContextParameterPreparedArgument<PARSED> implements ContextRequirement<SENDER, PARSED>, ParameterRequirement<SENDER, PARSED> {

        private final Parameter parameter;
        private final int index;
        private final WrapFormat<PARSED, ?> wrapFormat;
        private final Wrapper wrapper;

        private ContextParameterPreparedArgument(Parameter parameter, int index, WrapFormat<PARSED, ?> wrapFormat, Wrapper wrapper) {
            this.parameter = parameter;
            this.index = index;
            this.wrapFormat = wrapFormat;
            this.wrapper = wrapper;
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
        public Parameter getParameter() {
            return parameter;
        }

        @Override
        public int getParameterIndex() {
            return index;
        }

    }

}
