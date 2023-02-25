package dev.rollczi.litecommands.modern.annotation.command;

import dev.rollczi.litecommands.modern.annotation.argument.ParameterArgumentContextualCreator;
import dev.rollczi.litecommands.modern.annotation.contextual.ParameterContextual;
import dev.rollczi.litecommands.modern.annotation.contextual.ParameterContextualCreator;
import dev.rollczi.litecommands.modern.argument.ArgumentResolverRegistry;
import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextualService;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MethodCommandExecutorFactory<SENDER> {

    private final List<Function<Parameter, List<ParameterContextual<?>>>> creators = new ArrayList<>();
    private final ArgumentResolverRegistry<SENDER> argumentResolverRegistry;

    private MethodCommandExecutorFactory(ArgumentResolverRegistry<SENDER> argumentResolverRegistry) {
        this.argumentResolverRegistry = argumentResolverRegistry;
    }

    public CommandExecutor<SENDER> create(Object instance, Method method) {
        List<ParameterContextual<?>> expectedContextuals = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            for (Function<Parameter, List<ParameterContextual<?>>> creator : this.creators) {
                List<ParameterContextual<?>> contextuals = creator.apply(parameter);

                if (contextuals.isEmpty()) {
                    continue;
                }

                expectedContextuals.addAll(contextuals);
                break;
            }
        }

        return MethodCommandExecutor.of(method, instance, expectedContextuals, argumentResolverRegistry);
    }

    public static <SENDER> MethodCommandExecutorFactory<SENDER> create(WrappedExpectedContextualService wrappedExpectedContextualService, ArgumentResolverRegistry<SENDER> argumentResolverRegistry) {
        MethodCommandExecutorFactory<SENDER> factory = new MethodCommandExecutorFactory<>(argumentResolverRegistry);

        factory.creators.add(new ParameterArgumentContextualCreator(wrappedExpectedContextualService));
        factory.creators.add(new ParameterContextualCreator(wrappedExpectedContextualService));

        return factory;
    }

}
