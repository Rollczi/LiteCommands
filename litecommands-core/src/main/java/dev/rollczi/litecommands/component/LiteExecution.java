package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.annotations.parser.AnnotationParser;
import dev.rollczi.litecommands.inject.InjectContext;
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
        Result<Option<Object>, Pair<String, Throwable>> result = executor.execute(new InjectContext(context, this));

        if (result.isOk()) {
            return ExecutionResult.valid();
        }

        Throwable throwable = result.getError().getSecond();
        String errorMessage = result.getError().getFirst();

        if (throwable instanceof ValidationCommandException) {
            ValidationCommandException validationEx = (ValidationCommandException) throwable;

            return ExecutionResult.invalid(validationEx.getValidationInfo(), validationEx.getMessage());
        }

        logger.log(Level.SEVERE, errorMessage, throwable);
        return ExecutionResult.invalid(ValidationInfo.INTERNAL_ERROR, errorMessage);
    }

    @Override
    public List<String> resolveCompletion(ContextOfResolving context) {
        LiteInvocation invocation = context.getInvocation();

        return generateCompletion(context.getCurrentArgsCount(this) - 1, invocation.alias(), invocation.arguments());
    }

    public List<String> generateCompletion(int argNumber, String commandName, String[] commandArgs) {
        return executor.getParameterClass(argNumber)
                .flatMap(parser::getArgumentHandler)
                .map(argumentHandler -> argumentHandler.tabulation(commandName, commandArgs))
                .orElseGet(Collections.emptyList());
    }

    public MethodExecutor getExecutor() {
        return executor;
    }

}
