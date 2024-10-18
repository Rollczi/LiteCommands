
package dev.rollczi.litecommands.annotations.argument.profile;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.MutableArgument;
import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectInvocationException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;

public abstract class ProfileAnnotationProcessor<SENDER, A extends Annotation, PROFILE extends ArgumentProfile<PROFILE>> implements AnnotationProcessor<SENDER> {

    private final Class<A> annotationType;

    protected ProfileAnnotationProcessor(Class<A> annotationType) {
        this.annotationType = annotationType;
    }

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onParameterRequirement(annotationType, (parameter, annotation, builder, requirement) -> {
            if (!(requirement instanceof MutableArgument)) {
                Executable declaringExecutable = parameter.getDeclaringExecutable();
                throw new LiteCommandsReflectInvocationException(declaringExecutable, parameter, "@" + annotationType.getSimpleName() + " can be used only on arguments: " + requirement.getClass().getName());
            }

            MutableArgument<?> argument = (MutableArgument<?>) requirement;
            PROFILE profile = createProfile(parameter, annotation, argument);

            if (profile == null) {
                return;
            }

            argument.addProfile(profile);
        });
    }

    protected abstract PROFILE createProfile(Parameter parameter, A annotation, Argument<?> argument);

}

