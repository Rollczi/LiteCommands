package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.sugesstion.Suggester;
import panda.std.Option;

import java.lang.annotation.Annotation;

public interface AnnotatedParameter<A extends Annotation> {

    A annotation();

    Argument<A> argument();

    Option<String> name();

    Option<String> schematic();

    Suggester toSuggester(LiteInvocation invocation);

}
