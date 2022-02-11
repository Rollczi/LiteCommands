package dev.rollczi.litecommands.bind.basic;

import dev.rollczi.litecommands.annotations.Arg;
import dev.rollczi.litecommands.annotations.Handler;
import dev.rollczi.litecommands.annotations.parser.AnnotationParser;
import dev.rollczi.litecommands.argument.ArgumentHandler;
import dev.rollczi.litecommands.bind.NativeBind;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.utils.InjectUtils;
import org.panda_lang.utilities.inject.Resources;

import java.util.Map;
import java.util.Set;

public class ArgAnnotationBind implements NativeBind {

    @Override
    public void bind(AnnotationParser annotationParser, Resources resources) {
        resources.annotatedWith(Arg.class).assignHandler((property, arg, objects) -> {
            LiteComponent.ContextOfResolving context = InjectUtils.getContextFromObjects(objects);
            int value = arg.value();

            for (Map.Entry<Class<?>, Set<ArgumentHandler<?>>> entry : annotationParser.getArgumentHandlers().entrySet()) {
                Class<?> on = entry.getKey();
                Set<ArgumentHandler<?>> handlers = entry.getValue();

                if (!on.isAssignableFrom(property.getType())) {
                    continue;
                }

                Handler handler = property.getAnnotation(Handler.class);

                for (ArgumentHandler<?> argumentHandler : handlers) {
                    if (handler != null && !handler.value().equals(ArgumentHandler.class) && !handler.value().equals(argumentHandler.getNativeClass())) {
                        continue;
                    }

                    return argumentHandler.parse(context, value);
                }
            }

            return null;
        });
    }

}
