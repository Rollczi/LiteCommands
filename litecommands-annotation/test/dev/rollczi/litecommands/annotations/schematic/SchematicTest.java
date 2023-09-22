package dev.rollczi.litecommands.annotations.schematic;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.flag.Flag;
import dev.rollczi.litecommands.async.Async;
import dev.rollczi.litecommands.command.Command;
import dev.rollczi.litecommands.execute.Execute;
import dev.rollczi.litecommands.permission.Permission;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.unit.TestPlatformSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import panda.std.Option;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class SchematicTest extends LiteTestSpec {

    static class ServerRank {}

    @Command(name = "rank")
    @Permission("server.management.rank")
    static class RankCommand {
        @Execute(name = "list")
        void list(@Arg("rank") Option<ServerRank> rank) {}

        @Async
        @Execute(name = "set")
        void set(@Arg("target") String target, @Arg("rank") Option<ServerRank> rank, @Arg("duration") Option<String> duration) {}

        @Execute(name = "info")
        void info(@Arg("rank") ServerRank rank) {}

        @Async
        @Execute(name = "create")
        void create(@Arg("name") String name, @Arg("color") String color, @Arg("priority") int priority, @Arg("displayname") Option<String> displayName) {}

        @Async
        @Execute(name = "delete")
        void delete(@Arg("rank") ServerRank rank, @Flag("-confirm") boolean confirmation) {}

        @Execute(name = "permission info")
        void permissionInfo(@Arg("rank") ServerRank rank, @Arg("permission") Option<String> permission) {}

        @Execute(name = "permission add")
        void permissionAdd(@Arg("permission") String permission) {}

        @Execute(name = "permission remove")
        void permissionRemove(@Arg("permission") String permission) {}
    }

    @Test
    @DisplayName("Should generate schematic for all commands")
    void shouldGenerateSchematicForAllCommands() {
        InvalidUsage invalidUsage = platform.execute(TestPlatformSender.permittedAll(), "rank")
            .assertFailedAs(InvalidUsage.class);

        assertThat(invalidUsage.getCause())
            .isEqualByComparingTo(InvalidUsage.Cause.UNKNOWN_COMMAND);

        assertThat(invalidUsage.getSchematic().all())
            .containsOnly(
                "/rank list [rank]",
                "/rank set <target> [rank] [duration]",
                "/rank info <rank>",
                "/rank create <name> <color> <priority> [displayname]",
                "/rank delete <rank> [-confirm]",
                "/rank permission info <rank> [permission]",
                "/rank permission add <permission>",
                "/rank permission remove <permission>"
            );
    }

}
