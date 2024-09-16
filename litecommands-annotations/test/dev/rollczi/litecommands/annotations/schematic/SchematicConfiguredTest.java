
package dev.rollczi.litecommands.annotations.schematic;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.shortcut.Shortcut;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import dev.rollczi.litecommands.unit.TestPlatformSender;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SchematicConfiguredTest extends LiteTestSpec {

    static LiteConfig config = builder -> builder
        .schematicGenerator(SchematicFormat.parentheses());

    static class ServerRank {}

    @Command(name = "rank")
    @Permission("server.management.rank")
    static class RankCommand {

        @Execute
        @Shortcut("r")
        void rank(@Arg("test") String test) {}

        @Execute(name = "list")
        void list(@Arg("rank") Optional<ServerRank> rank) {}

        @Async
        @Execute(name = "set")
        void set(@Arg("target") String target, @Arg("rank") Optional<ServerRank> rank, @Arg("duration") Optional<String> duration) {}

        @Execute(name = "info")
        @Shortcut("r info")
        void info(@Arg("rank") ServerRank rank) {}

        @Async
        @Execute(name = "create")
        void create(@Arg("name") String name, @Arg("color") String color, @Arg("priority") int priority, @Arg("displayname") Optional<String> displayName) {}

        @Async
        @Execute(name = "delete")
        void delete(@Arg("rank") ServerRank rank, @Flag("-confirm") boolean confirmation) {}

        @Execute(name = "permission info")
        void permissionInfo(@Arg("rank") ServerRank rank, @Arg("permission") Optional<String> permission) {}

        @Execute(name = "permission add", aliases = {"perm add"})
        void permissionAdd(@Arg("permission") String permission) {}

        @Execute(name = "permission remove", aliases = {"perm remove"})
        void permissionRemove(@Arg("permission") String permission) {}
    }

    @Test
    @DisplayName("Should generate schematic for all commands")
    void shouldGenerateSchematicForAllCommands() {
        InvalidUsage invalidUsage = platform.execute(TestPlatformSender.permittedAll(), "rank")
            .assertFailedAs(InvalidUsage.class);

        assertThat(invalidUsage.getCause())
            .isEqualByComparingTo(InvalidUsage.Cause.MISSING_ARGUMENT);

        assertThat(invalidUsage.getSchematic().all())
            .containsOnly(
                "/rank (test)",
                "/rank list (rank)",
                "/rank set (target) (rank) (duration)",
                "/rank info (rank)",
                "/rank create (name) (color) (priority) (displayname)",
                "/rank delete (rank) (-confirm)",
                "/rank permission info (rank) (permission)",
                "/rank permission add (permission)",
                "/rank permission remove (permission)"
            );
    }

    @Test
    @DisplayName("Should generate schematic for all commands (with shortcuts)")
    void shouldGenerateSchematicForAllCommandsWithShortcuts() {
        InvalidUsage invalidUsage = platform.execute(TestPlatformSender.permittedAll(), "r")
            .assertFailedAs(InvalidUsage.class);

        assertThat(invalidUsage.getCause())
            .isEqualByComparingTo(InvalidUsage.Cause.MISSING_ARGUMENT);

        assertThat(invalidUsage.getSchematic().all())
            .containsOnly(
                "/r (test)",
                "/r info (rank)"
            );
    }

}
