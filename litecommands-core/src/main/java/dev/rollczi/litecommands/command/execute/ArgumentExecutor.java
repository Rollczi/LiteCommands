package dev.rollczi.litecommands.command.execute;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.amount.AmountValidator;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;

import java.util.List;

public interface ArgumentExecutor<SENDER> extends MetaHolder {

    ExecuteResult execute(Invocation<SENDER> invocation, FindResult<SENDER> findResult);

    FindResult<SENDER> find(LiteInvocation invocation, int route, FindResult<SENDER> lastResult);

    List<Argument<SENDER, ?>> arguments();

    List<AnnotatedParameter<SENDER, ?>> annotatedParameters();

    AmountValidator amountValidator();

    List<Suggestion> firstSuggestions(LiteInvocation invocation);

    @Override
    CommandMeta meta();

}
