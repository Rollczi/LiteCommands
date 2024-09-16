package dev.rollczi.litecommands.annotations.shortcut;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.argument.resolver.standard.NumberArgumentResolver;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static dev.rollczi.litecommands.unit.TestPlatformSender.permitted;

class ShortcutMultilevelTest extends LiteTestSpec {

    @Command(name = "team manage")
    @Permission("team.manage")
    static class TestMultiCommand {

        @Execute(name = "add")
        @Shortcut("team-add")
        @Permission("team.manage.add")
        String executeOpt(@Arg("player") String player, @Arg Optional<String> test) {
            return player + ":" + test.orElse("none");
        }

        @Execute(name = "remove")
        @Shortcut("team remove")
        @Permission("team.manage.remove")
        String executeRemove(@Arg int number, @Arg int number2) {
            return number + ":" + number2;
        }

        @Execute(name = "info")
        @Shortcut("team info")
        @Permission("team.manage.info")
        String executeInfo() {
            return "info";
        }

    }

    @Test
    void testExecuteShortcuts() {
        platform.execute(permitted("team.manage", "team.manage.add"), "team-add key value")
            .assertSuccess("key:value");
        platform.execute(permitted("team.manage", "team.manage.remove"), "team remove 1 2")
            .assertSuccess("1:2");
        platform.execute(permitted("team.manage", "team.manage.info"), "team info")
            .assertSuccess("info");
    }

    @Test
    void testExecute() {
        platform.execute(permitted("team.manage", "team.manage.add"), "team manage add key value")
            .assertSuccess("key:value");
        platform.execute(permitted("team.manage", "team.manage.remove"), "team manage remove 1 2")
            .assertSuccess("1:2");
        platform.execute(permitted("team.manage", "team.manage.info"), "team manage info")
            .assertSuccess("info");
    }

    @Test
    void testExecuteShortcutsWithoutPermission() {
        platform.execute("team-add key value")
            .assertMissingPermission("team.manage.add", "team.manage");
        platform.execute("team remove 1 2")
            .assertMissingPermission("team.manage.remove", "team.manage");
        platform.execute("team info")
            .assertMissingPermission("team.manage.info", "team.manage");
    }

    @Test
    void testExecuteWithoutPermission() {
        platform.execute("team manage add key value")
            .assertMissingPermission("team.manage.add", "team.manage");
        platform.execute("team manage remove 1 2")
            .assertMissingPermission("team.manage.remove", "team.manage");
        platform.execute("team manage info")
            .assertMissingPermission("team.manage.info", "team.manage");
    }

    @Test
    void testSuggestionShortcuts() {
        platform.suggest(permitted("team.manage", "team.manage.add"), "team-add ")
            .assertSuggest("<player>");
        platform.suggest(permitted("team.manage", "team.manage.remove"), "team remove ")
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), "");
        platform.suggest(permitted("team.manage", "team.manage.info"), "team info ")
            .assertSuggest();
    }

    @Test
    void testSuggestion() {
        platform.suggest(permitted("team.manage", "team.manage.add"), "team manage add ")
            .assertSuggest("<player>");
        platform.suggest(permitted("team.manage", "team.manage.remove"), "team manage remove ")
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), "");
        platform.suggest(permitted("team.manage", "team.manage.info"), "team manage info ")
            .assertSuggest();
    }

    @Test
    void testSuggestionShortcutsWithoutPermission() {
        platform.suggest(permitted("team.manage.add"), "team-add ")
            .assertSuggest();
    }

    @Test
    void testSuggestionWithoutPermission() {
        platform.suggest(permitted("team.manage.add"), "team manage add ")
            .assertSuggest();
    }

}
