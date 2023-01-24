package dev.rollczi.litecommands.modern.annotation.command;

import dev.rollczi.litecommands.modern.annotation.argument.ParameterArgumentContextualCreator;
import dev.rollczi.litecommands.modern.annotation.contextual.ParameterContextual;
import dev.rollczi.litecommands.modern.annotation.contextual.ParameterContextualCreator;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextualService;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MethodCommandExecutorFactory {

    private final List<Function<Parameter, List<ParameterContextual<?>>>> creators = new ArrayList<>();

    private MethodCommandExecutorFactory() {
    }

    private MethodCommandExecutor create(Object instance, Method method) {
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

        return new MethodCommandExecutor(method, expectedContextuals, instance);
    }

    public static MethodCommandExecutorFactory create(WrappedExpectedContextualService wrappedExpectedContextualService) {
        MethodCommandExecutorFactory factory = new MethodCommandExecutorFactory();

        factory.creators.add(new ParameterArgumentContextualCreator(wrappedExpectedContextualService));
        factory.creators.add(new ParameterContextualCreator(wrappedExpectedContextualService));

        return factory;
    }

}
