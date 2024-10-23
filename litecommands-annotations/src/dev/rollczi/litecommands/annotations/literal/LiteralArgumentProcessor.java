package dev.rollczi.litecommands.annotations.literal;

import dev.rollczi.litecommands.annotations.argument.profile.ProfileAnnotationProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.literal.LiteralProfile;
import java.lang.reflect.Parameter;

public class LiteralArgumentProcessor<SENDER> extends ProfileAnnotationProcessor<SENDER, Literal, LiteralProfile> {

    public LiteralArgumentProcessor() {
        super(Literal.class);
    }

    @Override
    protected LiteralProfile createProfile(Parameter parameter, Literal annotation, Argument<?> argument) {
        return new LiteralProfile(annotation.value(), annotation.ignoreCase());
    }

}
