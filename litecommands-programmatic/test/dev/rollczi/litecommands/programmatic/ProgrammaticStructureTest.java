package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.unit.TestSender;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.Test;

class ProgrammaticStructureTest {

    @Test
    void test() {
        LiteCommand<TestSender> execute = new LiteCommand<TestSender>("first second")
            .execute(context -> {});

        CommandBuilder<TestSender> route = execute.toRoute();

        assertThat(route.name()).isEqualTo("first");
        assertThat(route.executors())
            .hasSize(0);

        assertThat(route.children())
            .hasSize(1)
            .first()
            .satisfies(child -> {
                assertThat(child.name()).isEqualTo("second");
                assertThat(child.children())
                    .isEmpty();

                assertThat(child.executors())
                    .hasSize(1);
            });
    }

}
