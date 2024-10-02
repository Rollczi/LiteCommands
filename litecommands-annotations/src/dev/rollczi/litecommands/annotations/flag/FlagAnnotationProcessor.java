package dev.rollczi.litecommands.annotations.flag;

import dev.rollczi.litecommands.annotations.argument.profile.ProfileAnnotationProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.flag.FlagProfile;
import java.lang.reflect.Parameter;

public class FlagAnnotationProcessor<SENDER> extends ProfileAnnotationProcessor<SENDER, Flag, FlagProfile> {

    public FlagAnnotationProcessor() {
        super(Flag.class, FlagProfile.NAMESPACE);
    }

    @Override
    protected FlagProfile createProfile(Parameter parameter, Flag annotation, Argument<?> argument) {
        return new FlagProfile(annotation.value());
    }

}
