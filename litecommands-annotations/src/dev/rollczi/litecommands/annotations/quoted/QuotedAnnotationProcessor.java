
package dev.rollczi.litecommands.annotations.quoted;

import dev.rollczi.litecommands.annotations.argument.profile.ProfileAnnotationProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.quoted.QuotedProfile;
import java.lang.reflect.Parameter;

public class QuotedAnnotationProcessor<SENDER> extends ProfileAnnotationProcessor<SENDER, Quoted, QuotedProfile> {

    public QuotedAnnotationProcessor() {
        super(Quoted.class);
    }

    @Override
    protected QuotedProfile createProfile(Parameter parameter, Quoted annotation, Argument<?> argument) {
        return new QuotedProfile();
    }

}

