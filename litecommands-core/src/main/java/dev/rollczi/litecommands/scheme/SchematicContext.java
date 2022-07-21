package dev.rollczi.litecommands.scheme;

import dev.rollczi.litecommands.argument.AnnotatedParameter;
import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.section.CommandSection;

import java.util.List;
import java.util.Optional;

public interface SchematicContext<SENDER> {

    Invocation<SENDER> getInvocation();

    List<CommandSection<SENDER>> getSections();

    Optional<ArgumentExecutor<SENDER>> getExecutor();

    List<AnnotatedParameter<SENDER, ?>> getAllArguments();

}
