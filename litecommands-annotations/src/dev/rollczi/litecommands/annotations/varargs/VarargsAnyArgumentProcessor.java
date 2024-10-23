package dev.rollczi.litecommands.annotations.varargs;

import dev.rollczi.litecommands.annotations.argument.profile.ProfileAnnotationProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.collector.VarargsProfile;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Collection;

public class VarargsAnyArgumentProcessor<SENDER, A extends Annotation> extends ProfileAnnotationProcessor<SENDER, A, VarargsProfile> {

    public VarargsAnyArgumentProcessor(Class<A> annotationClass) {
        super(annotationClass);
    }

    @Override
    protected VarargsProfile createProfile(Parameter parameter, A annotation, Argument<?> argument) {
        TypeToken<?> collectorType = TypeToken.ofParameter(parameter);

        if (collectorType.isArray()) {
            return new VarargsProfile(collectorType.getComponentTypeToken(), RawCommand.COMMAND_SEPARATOR);
        }

        if (collectorType.isInstanceOf(Collection.class)) {
            return new VarargsProfile(collectorType.getParameterized(), RawCommand.COMMAND_SEPARATOR);
        }

        return null;
    }

}
