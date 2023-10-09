package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.unit.TestExecutor;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JDACommandTranslatorTest {

    public static final String COMMAND = "siema";
    public static final String SUB_COMMAND = "sub";
    public static final String NESTED_SUB_COMMAND = "subofsub";
    public static final String DESCRIPTION = "description";
    public static final String STRING_ARG = "name";
    public static final String INT_ARG = "age";

    final JDACommandTranslator translator = new JDACommandTranslator(new WrapperRegistry())
        .type(String.class,       OptionType.STRING,      option -> option.getAsString())
        .type(Integer.class,      OptionType.INTEGER,     option -> option.getAsInt());

    @Test
    void test() {
        CommandRoute<TestSender> root = CommandRoute.createRoot();
        CommandRoute<TestSender> siema = CommandRoute.create(root, COMMAND, List.of());
        siema.meta().put(Meta.DESCRIPTION, DESCRIPTION);
        siema.appendExecutor(simpleExecutor(siema));

        JDACommandTranslator.JDALiteCommand translated = translator.translate(COMMAND, siema);
        SlashCommandData slashCommandData = translated.jdaCommandData();

        assertEquals(COMMAND, slashCommandData.getName());
        assertEquals(DESCRIPTION, slashCommandData.getDescription());
        assertEquals(2, slashCommandData.getOptions().size());

        OptionData name = slashCommandData.getOptions().get(0);
        assertEquals(STRING_ARG, name.getName());
        assertEquals(OptionType.STRING, name.getType());
        assertTrue(name.isRequired());

        OptionData age = slashCommandData.getOptions().get(1);
        assertEquals(INT_ARG, age.getName());
        assertEquals(OptionType.INTEGER, age.getType());
        assertTrue(age.isRequired());
    }

    @Test
    void subcommandsAndGroup() {
        CommandRoute<TestSender> root = CommandRoute.createRoot();
        CommandRoute<TestSender> command = CommandRoute.create(root, COMMAND, List.of());
        command.meta().put(Meta.DESCRIPTION, DESCRIPTION);

        CommandRoute<TestSender> subCommand = CommandRoute.create(command, SUB_COMMAND, List.of());
        command.appendChildren(subCommand);
        subCommand.appendExecutor(simpleExecutor(subCommand));

        CommandRoute<TestSender> nestedSubCommand = CommandRoute.create(command, NESTED_SUB_COMMAND, List.of());
        subCommand.appendChildren(nestedSubCommand);
        nestedSubCommand.appendExecutor(simpleExecutor(subCommand));

        JDACommandTranslator.JDALiteCommand translated = translator.translate(COMMAND, command);
        SlashCommandData slashCommandData = translated.jdaCommandData();

        assertEquals(COMMAND, slashCommandData.getName());
        assertEquals(DESCRIPTION, slashCommandData.getDescription());
        assertEquals(1, slashCommandData.getSubcommands().size());

        {
            SubcommandData subcommandData = slashCommandData.getSubcommands().get(0);
            assertEquals(SUB_COMMAND, subcommandData.getName());
            assertEquals(2, subcommandData.getOptions().size());

            OptionData name = subcommandData.getOptions().get(0);
            assertEquals(STRING_ARG, name.getName());
            assertEquals(OptionType.STRING, name.getType());
            assertTrue(name.isRequired());

            OptionData age = subcommandData.getOptions().get(1);
            assertEquals(INT_ARG, age.getName());
            assertEquals(OptionType.INTEGER, age.getType());
            assertTrue(age.isRequired());
        }

        {
            SubcommandGroupData subcommandGroupData = slashCommandData.getSubcommandGroups().get(0);
            assertEquals(SUB_COMMAND, subcommandGroupData.getName());
            assertEquals(1, subcommandGroupData.getSubcommands().size());

            SubcommandData subcommandData = subcommandGroupData.getSubcommands().get(0);
            assertEquals(NESTED_SUB_COMMAND, subcommandData.getName());
            assertEquals(2, subcommandData.getOptions().size());

            OptionData name = subcommandData.getOptions().get(0);
            assertEquals(STRING_ARG, name.getName());
            assertEquals(OptionType.STRING, name.getType());

            OptionData age = subcommandData.getOptions().get(1);
            assertEquals(INT_ARG, age.getName());
            assertEquals(OptionType.INTEGER, age.getType());
        }

    }

    private TestExecutor<TestSender> simpleExecutor(CommandRoute<TestSender> test) {
        return new TestExecutor<>(test)
            .withArg(STRING_ARG, String.class, (invocation, argument) -> ParseResult.success(argument))
            .withArg(INT_ARG, Integer.class, (invocation, argument) -> ParseResult.success(Integer.parseInt(argument)));
    }

}