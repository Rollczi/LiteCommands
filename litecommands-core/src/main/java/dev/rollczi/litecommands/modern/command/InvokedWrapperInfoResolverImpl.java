package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.bind.BindRegistry;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpected;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpectedService;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.wrapper.WrapperFormat;
import panda.std.Option;
import panda.std.Result;

import java.util.function.Supplier;

class InvokedWrapperInfoResolverImpl<SENDER> implements InvokedWrapperInfoResolver<SENDER> {

    private final BindRegistry<SENDER> bindRegistry;
    private final WrappedExpectedService wrappedExpectedService;

    InvokedWrapperInfoResolverImpl(
        BindRegistry<SENDER> bindRegistry,
        WrappedExpectedService wrappedExpectedService
    ) {
        this.bindRegistry = bindRegistry;
        this.wrappedExpectedService = wrappedExpectedService;
    }

    @Override
    public <EXPECTED> Result<Supplier<WrappedExpected<EXPECTED>>, FailedReason> resolve(Invocation<SENDER> invocation, WrapperFormat<EXPECTED> format) {
        return this.provideContextual(invocation, format);
    }

    private <EXPECTED> Result<Supplier<WrappedExpected<EXPECTED>>, FailedReason> provideContextual(Invocation<SENDER> invocation, WrapperFormat<EXPECTED> wrapperFormat) {
        EXPECTED instance = bindRegistry.getInstance(wrapperFormat.getType(), invocation);

        if (instance != null) {
            return Result.ok(() -> wrappedExpectedService.wrap(() -> instance, wrapperFormat));
        }

        Option<WrappedExpected<EXPECTED>> empty = wrappedExpectedService.empty(wrapperFormat);

        if (empty.isEmpty()) {
            return Result.error(FailedReason.of(new RuntimeException("Cannot find bind for " + wrapperFormat.getType().getName())));
        }

        return Result.ok(empty::get);
    }

}

