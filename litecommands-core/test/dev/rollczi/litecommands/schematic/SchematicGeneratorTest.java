package dev.rollczi.litecommands.schematic;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.permission.MissingPermissionValidator;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.unit.TestExecutor;
import dev.rollczi.litecommands.unit.TestUtil;
import dev.rollczi.litecommands.validator.ValidatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"rawtypes", "unchecked"})
class SchematicGeneratorTest {

    static ValidatorService validatorService = new ValidatorService();
    static SchematicGenerator schematicGenerator = SchematicGenerator.from(SchematicFormat.angleBrackets(), validatorService);

    @BeforeAll
    static void beforeAll() {
        validatorService.registerValidator(Scope.global(), new MissingPermissionValidator<>());
    }

    @Test
    void test() {
        // test
        CommandRoute testCommand = CommandRoute.create(CommandRoute.createRoot(), "test", Collections.emptyList());

        // test subtest
        CommandRoute subTestCommand = CommandRoute.create(testCommand, "subtest", Collections.emptyList());
        testCommand.appendChildren(subTestCommand);

        // test subtest <first>
        TestExecutor executorSubTest = new TestExecutor<>(subTestCommand)
            .withStringArg("first");
        subTestCommand.appendExecutor(executorSubTest);

        // test subtest <first> <second> (permission)
        TestExecutor executorSubTest2 = new TestExecutor<>(subTestCommand)
            .withStringArg("first")
            .withStringArg("second");
        executorSubTest2.meta().put(Meta.PERMISSIONS, Collections.singletonList("test.permission"));
        subTestCommand.appendExecutor(executorSubTest2);

        // test subtest2
        CommandRoute subTest2Command = CommandRoute.create(testCommand, "subtest2", Collections.emptyList());
        testCommand.appendChildren(subTest2Command);

        // test subtest2 <test>
        TestExecutor executorSubTest2_1 = new TestExecutor<>(subTest2Command)
            .withStringArg("first");
        subTest2Command.appendExecutor(executorSubTest2_1);

        // test subtest2 <first> <second>
        TestExecutor executorSubTest2_2 = new TestExecutor<>(subTest2Command)
            .withStringArg("first")
            .withStringArg("second");
        subTest2Command.appendExecutor(executorSubTest2_2);

        // test one two
        CommandRoute oneCommand = CommandRoute.create(testCommand, "one", Collections.emptyList());
        testCommand.appendChildren(oneCommand);

        CommandRoute twoCommand = CommandRoute.create(oneCommand, "two", Collections.emptyList());
        oneCommand.appendChildren(twoCommand);

        // test one two <first>

        TestExecutor executorMulti = new TestExecutor<>(twoCommand)
            .withStringArg("first");
        twoCommand.appendExecutor(executorMulti);

        assertSchematic(testCommand, null,
            "/test subtest <first>",
            "/test subtest2 <first>",
            "/test subtest2 <first> <second>",
            "/test one two <first>"
        );

        assertSchematic(subTestCommand, null,
            "/test subtest <first>"
        );

        assertSchematic(subTest2Command, null,
            "/test subtest2 <first>",
            "/test subtest2 <first> <second>"
        );

        assertSchematic(subTestCommand, executorSubTest,
            "/test subtest <first>"
        );

        assertSchematic(subTest2Command, executorSubTest2_1,
            "/test subtest2 <first>"
        );

        assertSchematic(subTest2Command, executorSubTest2_2,
            "/test subtest2 <first> <second>"
        );

        assertSchematic(oneCommand, null,
            "/test one two <first>"
        );

        assertSchematic(twoCommand, null,
            "/test one two <first>"
        );

        assertSchematic(twoCommand, executorMulti,
            "/test one two <first>"
        );
    }



    private void assertSchematic(CommandRoute<?> commandRoute, CommandExecutor<?> executor, String... expected) {
        Schematic schematic = schematicGenerator.generate(new SchematicInput(commandRoute, executor, TestUtil.invocation("")));
        List<String> schematicLines = schematic.all();
        assertEquals(expected.length, schematicLines.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], schematicLines.get(i));
        }
    }

}