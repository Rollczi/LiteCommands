package dev.rollczi.litecommands.context;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.Test;

class ContextResultTest {

    @Test
    void test() {
        ContextResult<String> flatted = ContextResult.ok(() -> "test")
            .map(s -> s + "!")
            .flatMap(s -> ContextResult.ok(() -> s + "!!"));

        assertThat(flatted.asFuture())
            .isCompletedWithValueMatching(result -> result.getSuccess().equals("test!!!"));
    }

}
