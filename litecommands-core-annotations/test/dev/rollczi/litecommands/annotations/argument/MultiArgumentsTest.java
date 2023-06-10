package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteConfigurator;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.route.Route;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.input.RawInput;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MultiArgumentsTest extends LiteTestSpec {

    @LiteConfigurator
    static LiteConfig configure() {
        return builder -> builder
            .argument(Position.class, new PositionArg<>());
    }

    static class PositionArg<S> implements MultipleArgumentResolver<S, Position> {

        @Override
        public ArgumentResult<Position> parse(Invocation<S> invocation, Argument<Position> argument, RawInput rawInput) {
            try {
                Position position = new Position(
                    rawInput.nextInt(),
                    rawInput.nextInt(),
                    rawInput.nextInt()
                );

                return ArgumentResult.success(position);
            }
            catch (NumberFormatException exception) {
                return ArgumentResult.failure(InvalidUsage.Cause.INVALID_ARGUMENT);
            }
        }

        @Override
        public Range getRange() {
            return Range.of(3);
        }

    }

    static class Position {
       int x;
       int y;
       int z;

        public Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    @Route(name = "test")
    static class TestCommand {
        @Execute
        void execute(@Arg String arg0, @Arg String arg1, @Arg Position position) {}
        @Execute(route = "sub")
        void executeSub(@Arg Position position) {}
    }

    @Test
    @DisplayName("Should pass because of correct arguments count")
    void testCorrect() {
        platform.execute("test arg0 arg1 1 2 3")
            .assertSuccess();

        platform.execute("test sub 1 2 3")
            .assertSuccess();
    }

    @Test
    @DisplayName("Should fail because of incorrect arguments count")
    void testIncorrect() {
        platform.execute("test")
            .assertFailedAs(InvalidUsage.Cause.class);

        platform.execute("test arg0")
            .assertFailedAs(InvalidUsage.Cause.class);

        platform.execute("test arg0 arg1")
            .assertFailedAs(InvalidUsage.Cause.class);

        platform.execute("test arg0 arg1 1")
            .assertFailedAs(InvalidUsage.Cause.class);

        platform.execute("test arg0 arg1 1 2")
            .assertFailedAs(InvalidUsage.Cause.class);

        platform.execute("test sub")
            .assertFailedAs(InvalidUsage.Cause.class);

        platform.execute("test sub 1")
            .assertFailedAs(InvalidUsage.Cause.class);

        platform.execute("test sub 1 2")
            .assertFailedAs(InvalidUsage.Cause.class);
    }

    @Test
    @DisplayName("Should fail because of incorrect input")
    void testIncorrectInput() {
        platform.execute("test arg0 arg1 1 2 a")
            .assertFailedAs(InvalidUsage.Cause.class);

        platform.execute("test sub 1 2 a")
            .assertFailedAs(InvalidUsage.Cause.class);
    }

}
