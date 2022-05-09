package dev.rollczi.litecommands.command.execute;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.ExecuteResult;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.amount.AmountValidator;

import java.util.List;

public interface ArgumentExecutor {

    ExecuteResult execute(LiteInvocation invocation, FindResult findResult);

    FindResult find(LiteInvocation invocation, int route, FindResult lastResult);

    List<Argument<?>> arguments();

    AmountValidator amountValidator();

}
