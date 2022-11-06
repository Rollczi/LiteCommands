package dev.rollczi.litecommands.command.route;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.CommandSection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.stream.Collectors;

import static dev.rollczi.litecommands.Assert.assertCollection;
import static dev.rollczi.litecommands.TestUtils.list;

class RouteTest {

    TestPlatform platform = TestFactory.withCommandsUniversalHandler(Command.class);

    @Route(name = "command")
    static class Command {
        @Execute(route = "execute-route")
        String executeRoute() {
            return "execute-route";
        }

        @Execute
        @Route(name = "route")
        String route() {
            return "route";
        }

        @Route(name = "inner-class")
        static class ClassRoute {
            @Execute
            String execute() {
                return "inner-class";
            }

            @Route(name = "inner-route")
            String innerRoute() {
                return "inner-route";
            }

            @Route(name = "inner-class-sub")
            static class ClassRouteSub {
                @Execute
                String execute() {
                    return "inner-class-sub";
                }
            }
        }
    }

    @CsvSource({
        "execute-route,execute-route",
        "route,route",
        "inner-class,inner-class",
        "inner-class inner-route,inner-route",
        "inner-class inner-class-sub,inner-class-sub"
    })
    @ParameterizedTest
    void testExecuteRoute(String route, String expected) {
        platform.execute("command", route.split(" "))
                .assertSuccess()
                .assertResult(expected);
    }

    @Test
    void testInternalChildSection() {
        List<String> subRoutes = platform.getSection("command")
            .childrenSection()
            .stream()
            .map(CommandSection::getName)
            .collect(Collectors.toList());

        assertCollection(list("execute-route", "route", "inner-class"), subRoutes);
    }

}
