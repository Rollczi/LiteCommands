package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.annotations.LiteCommandsAnnotations;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.argument.parser.ParserRegistryImpl;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DescriptionTest {

    public static final String SINGLE_COMMAND = "simple";
    public static final String SINGLE_DESCRIPTION = "description simple";
    public static final String SINGLE_DESCRIPTION_EXECUTOR = "description simple executor";

    @Command(name = SINGLE_COMMAND)
    @Description(SINGLE_DESCRIPTION)
    static class TestCommandSimple {

        @Execute
        @Description(SINGLE_DESCRIPTION_EXECUTOR)
        void simple(@Arg String name, @Arg Integer age) {
        }

    }

    public static final String MULTI_COMMAND = "multi";
    public static final String MULTI_DESCRIPTION = "description";

    public static final String MULTI_SUBCOMMAND_SUB1 = "sub-1";
    public static final String MULTI_SUBCOMMAND_SUB2 = "sub-2";
    public static final String MULTI_DESCRIPTION_SUB1 = "description sub-1";
    public static final String MULTI_DESCRIPTION_SUB2 = "description sub-2";

    @Command(name = MULTI_COMMAND)
    @Description(MULTI_DESCRIPTION)
    static class MultiTestCommand {

        @Execute(name = MULTI_SUBCOMMAND_SUB1)
        @Description(MULTI_DESCRIPTION_SUB1)
        void subCommand() {
        }

        @Execute(name = MULTI_SUBCOMMAND_SUB2)
        @Description(MULTI_DESCRIPTION_SUB2)
        void subCommand2() {
        }

    }

    public static final String MULTI_GROUP_COMMAND = "multi";
    public static final String MULTI_GROUP_SUBCOMMAND = "group";
    public static final String MULTI_GROUP_DESCRIPTION = "description";

    public static final String MULTI_GROUP_SUBCOMMAND_SUB1 = "sub-1";
    public static final String MULTI_GROUP_SUBCOMMAND_SUB2 = "sub-2";
    public static final String MULTI_GROUP_DESCRIPTION_SUB1 = "description sub-1";
    public static final String MULTI_GROUP_DESCRIPTION_SUB2 = "description sub-2";

    @Command(name = MULTI_GROUP_COMMAND + " " + MULTI_GROUP_SUBCOMMAND)
    @Description(MULTI_GROUP_DESCRIPTION)
    static class MultiGroupTestCommand {

        @Execute(name = MULTI_GROUP_SUBCOMMAND_SUB1)
        @Description(MULTI_GROUP_DESCRIPTION_SUB1)
        void subCommand() {
        }

        @Execute(name = MULTI_GROUP_SUBCOMMAND_SUB2)
        @Description(MULTI_GROUP_DESCRIPTION_SUB2)
        void subCommand2() {
        }

    }

    JDACommandTranslator translator = new JDACommandTranslator(new ParserRegistryImpl<>())
        .type(String.class,       OptionType.STRING, option -> option.getAsString())
        .type(Integer.class,      OptionType.INTEGER,     option -> option.getAsInt());

    static TestPlatform platform;

    @BeforeAll
    static void beforeAll() {
        LiteCommandsAnnotations<TestSender> commands = LiteCommandsAnnotations.of(
            new TestCommandSimple(),
            new MultiTestCommand(),
            new MultiGroupTestCommand()
        );

        platform = LiteCommandsTestFactory.startPlatform(builder -> builder
            .commands(commands)
        );
    }

    @Test
    void testSingleCommand() {
        CommandRoute<TestSender> command = platform.findCommand(SINGLE_COMMAND);
        JDACommandTranslator.JDALiteCommand translated = translator.translate(command);
        SlashCommandData slashCommandData = translated.jdaCommandData();

        assertThat(slashCommandData)
            .isNotNull()
            .satisfies(data -> {
                assertEquals(SINGLE_COMMAND, data.getName());
                assertEquals(SINGLE_DESCRIPTION_EXECUTOR, data.getDescription());
            });

        assertThat(slashCommandData.getOptions())
            .hasSize(2);
    }

    @Test
    void testMultiCommand() {
        CommandRoute<TestSender> command = platform.findCommand(MULTI_COMMAND);
        JDACommandTranslator.JDALiteCommand translated = translator.translate(command);
        SlashCommandData slashCommandData = translated.jdaCommandData();

        assertThat(slashCommandData)
            .isNotNull()
            .satisfies(data -> {
                assertEquals(MULTI_COMMAND, data.getName());
                assertEquals(MULTI_DESCRIPTION, data.getDescription());
            });

        assertThat(slashCommandData.getOptions())
            .hasSize(0);

        assertThat(slashCommandData.getSubcommands())
            .hasSize(2)
            .anySatisfy(sub -> {
                assertEquals(MULTI_SUBCOMMAND_SUB1, sub.getName());
                assertEquals(MULTI_DESCRIPTION_SUB1, sub.getDescription());
            })
            .anySatisfy(sub -> {
                assertEquals(MULTI_SUBCOMMAND_SUB2, sub.getName());
                assertEquals(MULTI_DESCRIPTION_SUB2, sub.getDescription());
            });
    }

    @Test
    void testMultilevelCommand() {
        CommandRoute<TestSender> command = platform.findCommand(MULTI_GROUP_COMMAND);
        JDACommandTranslator.JDALiteCommand translated = translator.translate(command);
        SlashCommandData slashCommandData = translated.jdaCommandData();

        assertThat(slashCommandData)
            .isNotNull()
            .satisfies(data -> {
                assertEquals(MULTI_GROUP_COMMAND, data.getName());
                assertEquals(MULTI_GROUP_DESCRIPTION, data.getDescription());
            });

        assertThat(slashCommandData.getSubcommandGroups())
            .hasSize(1)
            .first()
            .satisfies(subcommand -> {
                assertEquals(MULTI_GROUP_SUBCOMMAND, subcommand.getName());
                assertEquals(MULTI_GROUP_DESCRIPTION, subcommand.getDescription());

                assertThat(subcommand.getSubcommands())
                    .hasSize(2)
                    .anySatisfy(sub -> {
                        assertEquals(MULTI_GROUP_SUBCOMMAND_SUB1, sub.getName());
                        assertEquals(MULTI_GROUP_DESCRIPTION_SUB1, sub.getDescription());
                    })
                    .anySatisfy(sub -> {
                        assertEquals(MULTI_GROUP_SUBCOMMAND_SUB2, sub.getName());
                        assertEquals(MULTI_GROUP_DESCRIPTION_SUB2, sub.getDescription());
                    });
            });
    }

}
