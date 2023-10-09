package dev.rollczi.litecommands.prettyprint;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class PrettyPrintTest {

    @Test
    void test() throws NoSuchMethodException {
        Class<TestCommand> commandClass = TestCommand.class;
        Method test = commandClass.getDeclaredMethod("test", String.class, String.class);

        String content = PrettyPrint.formatClass(test, test.getParameters()[0], "^ error: Can not find resolver for 'String'");

        assertThat(content).isEqualTo(
            "@Command(name = \"test\")\n" +
                "public static class TestCommand {\n" +
                "    \n" +
                "    @Execute(name = \"test\", aliases = { \"t\", \"tt\" })\n" +
                "    public List<String> test(@Arg(value = \"arg\") String arg0, @Arg String arg1) {\n" +
                "                             ^ error: Can not find resolver for 'String'\n" +
                "    }\n" +
                "}"
        );
    }

    @Command(name = "test")
    public static class TestCommand {

        @Execute(name = "test", aliases = {"t", "tt"})
        public List<String> test(@Arg("arg") String argument, @Arg String argument2) {
            return Collections.emptyList();
        }

    }

}

