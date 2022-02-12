package dev.rollczi.litecommands.bind.basic;

import dev.rollczi.litecommands.annotations.Joiner;
import dev.rollczi.litecommands.annotations.parser.AnnotationParser;
import dev.rollczi.litecommands.bind.NativeBind;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.component.MethodExecutor;
import dev.rollczi.litecommands.utils.InjectUtils;
import org.panda_lang.utilities.inject.Resources;

import java.util.Arrays;
import java.util.List;

public class JoinerAnnotationBind implements NativeBind {

    @Override
    public void bind(AnnotationParser annotationParser, Resources resources) {
        resources.annotatedWith(Joiner.class).assignHandler((property, arg, objects) -> {
            LiteComponent.ContextOfResolving context = InjectUtils.getContextFromInjectorArgs(objects);
            MethodExecutor executor = InjectUtils.getMethodExecutorFromInjectorArgs(objects);
            List<LiteComponent> components = context.getTracesOfResolvers();

            if (components.size() == 0) {
                return null;
            }

            LiteComponent lastComponent = components.get(components.size() - 1);
            String lastComponentName = lastComponent.getScope().getName();

            int fakeComponents = lastComponentName.isEmpty() ? 1 : 0;
            int subCommandCount = (components.size() - 1) - fakeComponents;
            int argumentsCount = executor.getArgumentHandlers().size();
            int otherUnknownArgsIndex = subCommandCount + argumentsCount;

            String[] args = context.getInvocation().arguments();
            List<String> toJoin = Arrays.asList(args).subList(otherUnknownArgsIndex, args.length);

            return panda.utilities.text.Joiner.on(" ").join(toJoin).toString();
        });
    }

}
