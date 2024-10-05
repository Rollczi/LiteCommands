package dev.rollczi.litecommands.annotations.varargs;

import dev.rollczi.litecommands.annotations.argument.profile.ProfileAnnotationProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.collector.VarargsProfile;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.lang.reflect.Parameter;
import java.util.Collection;

public class VarargsArgumentProcessor<SENDER> extends ProfileAnnotationProcessor<SENDER, Varargs, VarargsProfile> {

    public VarargsArgumentProcessor() {
        super(Varargs.class);
    }

    @Override
    protected VarargsProfile createProfile(Parameter parameter, Varargs annotation, Argument<?> argument) {
        TypeToken<?> collectorType = TypeToken.ofParameter(parameter);

        if (collectorType.isArray()) {
            return new VarargsProfile(collectorType.getComponentTypeToken(), annotation.delimiter());
        }

        if (collectorType.isInstanceOf(Collection.class)) {
            return new VarargsProfile(collectorType.getParameterized(), annotation.delimiter());
        }

        return null;
    }

}
