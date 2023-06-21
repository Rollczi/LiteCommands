package dev.rollczi.litecommands.annotations.context;

import dev.rollczi.litecommands.annotations.command.ParameterRequirement;
import dev.rollczi.litecommands.annotations.command.ParameterWithAnnotationResolver;
import dev.rollczi.litecommands.annotations.util.WrapperParameterUtil;
import dev.rollczi.litecommands.argument.parser.input.ParsableInputMatcher;
import dev.rollczi.litecommands.command.requirement.RequirementResult;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class ContextAnnotationResolver<SENDER> implements ParameterWithAnnotationResolver<SENDER, Context> {

    private final ContextRegistry<SENDER> contextRegistry;
    private final WrapperRegistry wrapperRegistry;

    public ContextAnnotationResolver(ContextRegistry<SENDER> contextRegistry, WrapperRegistry wrapperRegistry) {
        this.contextRegistry = contextRegistry;
        this.wrapperRegistry = wrapperRegistry;
    }

    @Override
    public ParameterRequirement<SENDER, ?> resolve(Parameter parameter, Context annotation) {
        Method method = (Method) parameter.getDeclaringExecutable();
        int index = Arrays.asList(method.getParameters()).indexOf(parameter);
        WrapFormat<?, ?> wrapFormat = WrapperParameterUtil.wrapperFormat(wrapperRegistry, parameter);

        return new ContextParameterPreparedArgument<>(parameter, index, wrapFormat, wrapperRegistry.getWrappedExpectedFactory(wrapFormat));
    }

    private class ContextParameterPreparedArgument<EXPECTED> implements ParameterRequirement<SENDER, EXPECTED> {

        private final Parameter parameter;
        private final int index;
        private final WrapFormat<EXPECTED, ?> wrapFormat;
        private final Wrapper wrapper;

        private ContextParameterPreparedArgument(Parameter parameter, int index, WrapFormat<EXPECTED, ?> wrapFormat, Wrapper wrapper) {
            this.parameter = parameter;
            this.index = index;
            this.wrapFormat = wrapFormat;
            this.wrapper = wrapper;
        }

        @Override
        public <CONTEXT extends ParsableInputMatcher<CONTEXT>> RequirementResult<EXPECTED> match(Invocation<SENDER> invocation, CONTEXT matcher) {
            ContextResult<EXPECTED> result = contextRegistry.provideContext(wrapFormat.getParsedType(), invocation);

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
