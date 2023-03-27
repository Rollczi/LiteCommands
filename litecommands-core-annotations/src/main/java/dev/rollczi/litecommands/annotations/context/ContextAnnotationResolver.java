package dev.rollczi.litecommands.annotations.context;

import dev.rollczi.litecommands.annotations.command.ParameterPreparedArgument;
import dev.rollczi.litecommands.annotations.command.ParameterWithAnnotationResolver;
import dev.rollczi.litecommands.annotations.util.WrapperParameterUtil;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.PreparedArgumentResult;
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
    public ParameterPreparedArgument<SENDER, ?> resolve(Parameter parameter, Context annotation) {
        Method method = (Method) parameter.getDeclaringExecutable();
        int index = Arrays.asList(method.getParameters()).indexOf(parameter);
        WrapperFormat<?, ?> wrapperFormat = WrapperParameterUtil.wrapperFormat(wrappedExpectedService, parameter);

        return new ContextParameterPreparedArgument<>(parameter, index, wrapperFormat, wrappedExpectedService.getWrappedExpectedFactory(wrapperFormat));
    }

    private class ContextParameterPreparedArgument<EXPECTED> implements ParameterPreparedArgument<SENDER, EXPECTED> {

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
        public PreparedArgumentResult<EXPECTED> resolve(Invocation<SENDER> invocation, List<String> arguments) {
            Result<EXPECTED, Object> result = bindRegistry.getInstance(wrapperFormat.getType(), invocation);

            if (result.isOk()) {
                return PreparedArgumentResult.success(() -> wrappedExpectedFactory.create(result::get, wrapperFormat), 0);
            }

            return PreparedArgumentResult.failed(result.getError());
        }

        @Override
        public Range getRange() {
            return Range.ZERO;
        }

        @Override
        public WrapperFormat<EXPECTED, ?> getWrapperFormat() {
            return wrapperFormat;
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
