package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.command.executor.AbstractCommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class TestExecutor<SENDER> extends AbstractCommandExecutor<SENDER> {

    private final Object result;

    public TestExecutor(CommandRoute<SENDER> parent, Object result) {
        super(parent, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        this.result = result;
    }

    public TestExecutor(CommandRoute<SENDER> parent) {
        super(parent, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        this.result = null;
    }

    public TestExecutor<SENDER> withStringArg(String name) {
        return withArg(name, String.class, (invocation, input) -> ParseResult.success(input));
    }

    public <T> TestExecutor<SENDER> withArg(String name, Class<T> type, BiFunction<Invocation<SENDER>, String, ParseResult<T>> parser) {
        arguments.add(new SimpleArgument<>(name, WrapFormat.notWrapped(type)));
        return this;
    }

    @Override
    public CommandExecutorMatchResult match(RequirementsResult<SENDER> requirementsResult) {
        return CommandExecutorMatchResult.success(() -> CommandExecuteResult.success(this, this.result));
    }

    public TestExecutor<SENDER> onMeta(UnaryOperator<Meta> operator) {
        operator.apply(meta);
        return this;
    }

}
