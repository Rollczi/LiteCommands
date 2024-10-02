package dev.rollczi.litecommands.annotations.argument.collection;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.profile.ProfileAnnotationProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.collector.VarargsProfile;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.lang.reflect.Parameter;
import java.util.Collection;

public class ArgCollectionArgumentProcessor<SENDER> extends ProfileAnnotationProcessor<SENDER, Arg, VarargsProfile> {

    public ArgCollectionArgumentProcessor() {
        super(Arg.class, VarargsProfile.NAMESPACE);
    }

    @Override
    protected VarargsProfile createProfile(Parameter parameter, Arg annotation, Argument<?> argument) {
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
