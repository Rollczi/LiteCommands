package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.annotations.parser.AnnotationParser;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import dev.rollczi.litecommands.valid.ValidationInfo;
import panda.std.Option;
import panda.std.Pair;
import panda.std.Result;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LiteExecution extends AbstractComponent {

    private final Logger logger;
    private final AnnotationParser parser;
    private final MethodExecutor executor;

    LiteExecution(Logger logger, AnnotationParser parser, ScopeMetaData scopeMetaData, MethodExecutor executor) {
        super(scopeMetaData);
        this.logger = logger;
        this.parser = parser;
        this.executor = executor;
    }

    @Override
    public ExecutionResult resolveExecution(ContextOfResolving context) {
        ContextOfResolving currentContext = context.resolverNestingTracing(this);
        Result<Option<Object>, Pair<String, Throwable>> result = executor.execute(currentContext);

        if (result.isOk()) {
            return ExecutionResult.valid(currentContext);
        }

        Throwable throwable = result.getError().getSecond();
        String errorMessage = result.getError().getFirst();

        if (throwable instanceof ValidationCommandException) {
            ValidationCommandException validationEx = (ValidationCommandException) throwable;

            return ExecutionResult.invalid(validationEx.getValidationInfo(), validationEx.getMessage(), currentContext);
        }

        logger.log(Level.SEVERE, errorMessage, throwable);
        return ExecutionResult.invalid(ValidationInfo.INTERNAL_ERROR, errorMessage, context);
    }

    @Override
    public List<String> resolveCompletion(ContextOfResolving context) {
        return generateCompletion(context.getCurrentArgsCount(this) - 1, context);
    }

    public List<String> generateCompletion(int argNumber, LiteComponent.ContextOfResolving context) {
        return executor.getArgumentHandler(argNumber)
                .map(argumentHandler -> argumentHandler.tabulation(context))
                .orElseGet(Collections.emptyList());
    }

    public MethodExecutor getExecutor() {
        return executor;
    }

}
