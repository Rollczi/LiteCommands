package dev.rollczi.litecommands.annotations.context;

import dev.rollczi.litecommands.annotations.command.ParameterCommandRequirement;
import dev.rollczi.litecommands.annotations.command.ParameterWithAnnotationResolver;
import dev.rollczi.litecommands.annotations.util.WrapperParameterUtil;
import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.argument.input.InputArgumentsMatcher;
import dev.rollczi.litecommands.command.requirements.CommandRequirementResult;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.wrapper.WrappedExpectedFactory;
import dev.rollczi.litecommands.wrapper.WrappedExpectedService;
import dev.rollczi.litecommands.wrapper.WrapperFormat;
import panda.std.Result;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class ContextAnnotationResolver<SENDER> implements ParameterWithAnnotationResolver<SENDER, Context> {

    private final BindRegistry<SENDER> bindRegistry;
    private final WrappedExpectedService wrappedExpectedService;

    public ContextAnnotationResolver(BindRegistry<SENDER> bindRegistry, WrappedExpectedService wrappedExpectedService) {
        this.bindRegistry = bindRegistry;
        this.wrappedExpectedService = wrappedExpectedService;
    }

    @Override
    public ParameterCommandRequirement<SENDER, ?> resolve(Parameter parameter, Context annotation) {
        Method method = (Method) parameter.getDeclaringExecutable();
        int index = Arrays.asList(method.getParameters()).indexOf(parameter);
        WrapperFormat<?, ?> wrapperFormat = WrapperParameterUtil.wrapperFormat(wrappedExpectedService, parameter);

        return new ContextParameterPreparedArgument<>(parameter, index, wrapperFormat, wrappedExpectedService.getWrappedExpectedFactory(wrapperFormat));
    }

    private class ContextParameterPreparedArgument<EXPECTED> implements ParameterCommandRequirement<SENDER, EXPECTED> {

        private final Parameter parameter;
        private final int index;
        private final WrapperFormat<EXPECTED, ?> wrapperFormat;
        private final WrappedExpectedFactory wrappedExpectedFactory;

        private ContextParameterPreparedArgument(Parameter parameter, int index, WrapperFormat<EXPECTED, ?> wrapperFormat, WrappedExpectedFactory wrappedExpectedFactory) {
            this.parameter = parameter;
            this.index = index;
            this.wrapperFormat = wrapperFormat;
            this.wrappedExpectedFactory = wrappedExpectedFactory;
        }

        @Override
        public <CONTEXT extends InputArgumentsMatcher<CONTEXT>> CommandRequirementResult<EXPECTED> check(Invocation<SENDER> invocation, InputArguments<CONTEXT> inputArguments, CONTEXT context) {
            Result<EXPECTED, Object> result = bindRegistry.getInstance(wrapperFormat.getType(), invocation);

            if (result.isOk()) {
                return CommandRequirementResult.success(() -> wrappedExpectedFactory.create(result::get, wrapperFormat));
            }

            return CommandRequirementResult.failure(result.getError());
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
