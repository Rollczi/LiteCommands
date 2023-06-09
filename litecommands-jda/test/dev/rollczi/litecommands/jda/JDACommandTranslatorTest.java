package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.unit.TestExecutor;
import dev.rollczi.litecommands.unit.TestSender;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JDACommandTranslatorTest {

    JDACommandTranslator translator = new JDACommandTranslator()
        .type(String.class,       OptionType.STRING,      option -> option.getAsString())
        .type(Integer.class,         OptionType.INTEGER,     option -> option.getAsInt());

    @Test
    void test() {
        CommandRoute<TestSender> root = CommandRoute.createRoot();
        CommandRoute<TestSender> siema = CommandRoute.create(root, "siema", List.of());
        siema.meta().put(CommandMeta.DESCRIPTION, "description");
        siema.appendExecutor(simpleExecutor());

        JDACommandTranslator.JDALiteCommand translated = translator.translate("siema", siema);
        SlashCommandData slashCommandData = translated.jdaCommandData();

        assertEquals("siema", slashCommandData.getName());
        assertEquals("description", slashCommandData.getDescription());
        assertEquals(2, slashCommandData.getOptions().size());

        OptionData name = slashCommandData.getOptions().get(0);
        assertEquals("name", name.getName());
        assertEquals(OptionType.STRING, name.getType());
        assertTrue(name.isRequired());

        OptionData age = slashCommandData.getOptions().get(1);
        assertEquals("age", age.getName());
        assertEquals(OptionType.INTEGER, age.getType());
        assertTrue(age.isRequired());
    }

    @Test
    void subcommandsAndGroup() {
        CommandRoute<TestSender> root = CommandRoute.createRoot();
        CommandRoute<TestSender> siema = CommandRoute.create(root, "siema", List.of());
        siema.meta().put(CommandMeta.DESCRIPTION, "description");

        CommandRoute<TestSender> sub = CommandRoute.create(siema, "sub", List.of());
        siema.appendChildren(sub);
        sub.appendExecutor(simpleExecutor());

        CommandRoute<TestSender> subOfSub = CommandRoute.create(siema, "subofsub", List.of());
        sub.appendChildren(subOfSub);
        subOfSub.appendExecutor(simpleExecutor());

        JDACommandTranslator.JDALiteCommand translated = translator.translate("siema", siema);
        SlashCommandData slashCommandData = translated.jdaCommandData();

        assertEquals("siema", slashCommandData.getName());
        assertEquals("description", slashCommandData.getDescription());
        assertEquals(1, slashCommandData.getSubcommands().size());

        {
            SubcommandData subcommandData = slashCommandData.getSubcommands().get(0);
            assertEquals("sub", subcommandData.getName());
            assertEquals(2, subcommandData.getOptions().size());

            OptionData name = subcommandData.getOptions().get(0);
            assertEquals("name", name.getName());
            assertEquals(OptionType.STRING, name.getType());
            assertTrue(name.isRequired());

            OptionData age = subcommandData.getOptions().get(1);
            assertEquals("age", age.getName());
            assertEquals(OptionType.INTEGER, age.getType());
            assertTrue(age.isRequired());
        }

        {
            SubcommandGroupData subcommandGroupData = slashCommandData.getSubcommandGroups().get(0);
            assertEquals("sub", subcommandGroupData.getName());
            assertEquals(1, subcommandGroupData.getSubcommands().size());

            SubcommandData subcommandData = subcommandGroupData.getSubcommands().get(0);
            assertEquals("subofsub", subcommandData.getName());
            assertEquals(2, subcommandData.getOptions().size());

            OptionData name = subcommandData.getOptions().get(0);
            assertEquals("name", name.getName());
            assertEquals(OptionType.STRING, name.getType());

            OptionData age = subcommandData.getOptions().get(1);
            assertEquals("age", age.getName());
            assertEquals(OptionType.INTEGER, age.getType());
        }

    }

    private TestExecutor<TestSender> simpleExecutor() {
        return new TestExecutor<TestSender>()
            .withArg("name", String.class, (invocation, argument) -> ArgumentResult.success(argument))
            .withArg("age", Integer.class, (invocation, argument) -> ArgumentResult.success(Integer.parseInt(argument)));
    }

}