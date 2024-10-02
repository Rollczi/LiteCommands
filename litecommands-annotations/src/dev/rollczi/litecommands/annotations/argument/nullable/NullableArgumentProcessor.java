package dev.rollczi.litecommands.annotations.argument.nullable;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.profile.ProfileAnnotationProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.nullable.NullableProfile;
import java.lang.reflect.Parameter;

public class NullableArgumentProcessor<SENDER> extends ProfileAnnotationProcessor<SENDER, Arg, NullableProfile> {

    public NullableArgumentProcessor() {
        super(Arg.class, NullableProfile.NAMESPACE);
    }

    @Override
    protected NullableProfile createProfile(Parameter parameter, Arg annotation, Argument<?> argument) {
        if (annotation.nullable()) {
            return new NullableProfile();
        }

        return null;
    }

}
