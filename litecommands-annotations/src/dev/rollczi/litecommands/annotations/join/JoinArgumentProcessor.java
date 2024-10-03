package dev.rollczi.litecommands.annotations.join;

import dev.rollczi.litecommands.annotations.argument.profile.ProfileAnnotationProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.join.JoinProfile;
import java.lang.reflect.Parameter;

public class JoinArgumentProcessor<SENDER> extends ProfileAnnotationProcessor<SENDER, Join, JoinProfile> {

    public JoinArgumentProcessor() {
        super(Join.class);
    }

    @Override
    protected JoinProfile createProfile(Parameter parameter, Join annotation, Argument<?> argument) {
        return new JoinProfile(annotation.separator(), annotation.limit());
    }

}
