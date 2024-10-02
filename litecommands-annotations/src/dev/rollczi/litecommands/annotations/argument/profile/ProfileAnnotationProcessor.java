
package dev.rollczi.litecommands.annotations.argument.profile;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileNamespace;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectInvocationException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;

public abstract class ProfileAnnotationProcessor<SENDER, A extends Annotation, PROFILE> implements AnnotationProcessor<SENDER> {

    private final Class<A> annotationType;
    private final ArgumentProfileNamespace<PROFILE> key;

    protected ProfileAnnotationProcessor(Class<A> annotationType, ArgumentProfileNamespace<PROFILE> key) {
        this.annotationType = annotationType;
        this.key = key;
    }

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onParameterRequirement(annotationType, (parameter, annotationHolder, builder, requirement) -> {
            if (!(requirement instanceof Argument)) {
                Executable declaringExecutable = parameter.getDeclaringExecutable();
                throw new LiteCommandsReflectInvocationException(declaringExecutable, parameter, "@" + annotationType.getSimpleName() + " can be used only on arguments");
            }

            Argument<?> argument = (Argument<?>) requirement;
            PROFILE profile = createProfile(parameter, annotationHolder.getAnnotation(), argument);

            if (profile == null) {
                return;
            }

            argument.profiled(key, profile);
        });
    }

    protected abstract PROFILE createProfile(Parameter parameter, A annotation, Argument<?> argument);

}

