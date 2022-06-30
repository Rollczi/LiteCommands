package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.sugesstion.Suggester;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.util.List;

public interface AnnotatedParameter<SENDER, A extends Annotation> {

    A annotation();

    Argument<SENDER, A> argument();

    Option<String> name();

    List<Suggestion> staticSuggestions();

    Option<String> schematic();

    Suggester toSuggester(LiteInvocation invocation, int route);

}
