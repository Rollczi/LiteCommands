package dev.rollczi.litecommands.annotations.argument.resolver.nullable;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.Key;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class OptionalTest extends LiteTestSpec {

    interface IFaction {}

    static class Faction implements IFaction {}

    static LiteTestConfig config = builder -> builder
        .argumentParser(Faction.class, ArgumentKey.of("faction"), Parser.of((invocation, text) -> ParseResult.success(new Faction())))
        .argumentParser(IFaction.class, ArgumentKey.of("ifaction"), Parser.of((invocation, text) -> ParseResult.success(new Faction())))
        .advanced();

    @Command(name = "optional")
    static class OptionalTestCommand {
        @Execute
        String executeOptional(@Arg("display-faction") @Key("faction") Optional<Faction> faction) {
            return faction.isPresent() ? "present" : "not present";
        }

        @Execute(name = "interface")
        String executeInterfaceOptional(@OptionalArg("display-faction") @Key("ifaction") IFaction faction) {
            return faction != null ? "interface present" : "interface not present";
        }
    }

    @Test
    void testOptionalExecute() {
        platform.execute("optional")
            .assertSuccess("not present");

        platform.execute("optional faction")
            .assertSuccess("present");

        platform.execute("optional interface")
            .assertSuccess("interface not present");

        platform.execute("optional interface faction")
            .assertSuccess("interface present");
    }

}
