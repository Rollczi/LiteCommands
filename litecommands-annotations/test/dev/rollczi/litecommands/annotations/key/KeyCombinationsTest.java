package dev.rollczi.litecommands.annotations.key;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.Key;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import org.junit.jupiter.api.Test;

class KeyCombinationsTest extends LiteTestSpec {

    static class Faction {}

    static LiteTestConfig config = builder -> builder
        .argumentParser(Faction.class, Parser.of((invocation, text) -> ParseResult.success(new Faction())))
        .advanced();

    @RootCommand
    static class TestCommand {

        @Execute(name = "keyed")
        void keyed(@Arg("arg-name") @Key("faction") Faction argValue) {}

        @Execute(name = "undefined")
        void undefined(@Arg("arg-name") Faction argValue) {}

    }

    @Test
    void test() {
        platform.execute("keyed factionValue")
            .assertSuccess();

        platform.execute("undefined factionValue")
            .assertSuccess();
    }

}
