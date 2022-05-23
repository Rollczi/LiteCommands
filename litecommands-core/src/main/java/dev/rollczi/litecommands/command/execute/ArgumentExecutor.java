package dev.rollczi.litecommands.command.execute;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.amount.AmountValidator;
import dev.rollczi.litecommands.command.meta.Meta;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;

import java.util.List;

public interface ArgumentExecutor {

    ExecuteResult execute(LiteInvocation invocation, FindResult findResult);

    FindResult find(LiteInvocation invocation, int route, FindResult lastResult);

    List<Argument<?>> arguments();

    List<AnnotatedParameter<?>> annotatedParameters();

    Meta meta();

    AmountValidator amountValidator();

    List<Suggestion> firstSuggestions(LiteInvocation invocation);

}
