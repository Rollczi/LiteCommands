package dev.rollczi.litecommands.annotations.optional;

import dev.rollczi.litecommands.annotations.argument.profile.ProfileAnnotationProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.nullable.NullableProfile;
import java.lang.reflect.Parameter;

public class OptionalArgArgumentProcessor<SENDER> extends ProfileAnnotationProcessor<SENDER, OptionalArg, NullableProfile> {

    public OptionalArgArgumentProcessor() {
        super(OptionalArg.class);
    }

    @Override
    protected NullableProfile createProfile(Parameter parameter, OptionalArg annotation, Argument<?> argument) {
        return new NullableProfile();
    }

}
