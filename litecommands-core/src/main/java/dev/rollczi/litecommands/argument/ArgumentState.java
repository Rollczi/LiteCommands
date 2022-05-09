package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.command.Completion;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface ArgumentState {

    Argument<?> argument();

    Optional<String> name();

    Optional<String> scheme();

}
