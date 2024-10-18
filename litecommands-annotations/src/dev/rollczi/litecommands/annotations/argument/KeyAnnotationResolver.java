
package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.MutableArgument;

public class KeyAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onParameterRequirement(Key.class, (parameter, key, builder, requirement) -> {
            if (!(requirement instanceof MutableArgument<?>)) {
                throw new IllegalArgumentException("@Key annotation can be used only on arguments: " + requirement.getClass().getName());
            }

            MutableArgument<?> argument = (MutableArgument<?>) requirement;
            ArgumentKey argumentKey = argument.getKey();

            argument.setKey(argumentKey.withKey(key.value()));
        });
    }

}

