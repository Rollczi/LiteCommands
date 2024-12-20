package dev.rollczi.litecommands.bukkit.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistryImpl;
import dev.rollczi.litecommands.argument.resolver.optional.OptionalArgumentResolver;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.reflect.type.TypeRange;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.RequirementResult;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocationArgumentTest extends BukkitTestSpec {


    public static final Argument<Optional<Location>> ARGUMENT = Argument.of("location", new TypeToken<Optional<Location>>() {});
    private ParserRegistryImpl<CommandSender> parsers = new ParserRegistryImpl<>();

    @BeforeEach
    void before() {
        parsers.registerParser(Location.class, ArgumentKey.DEFAULT, new LocationArgument(new MessageRegistry<>()));
        parsers.registerParser(TypeRange.same(Optional.class), ArgumentKey.DEFAULT, new OptionalArgumentResolver<>(parsers));
    }

    @Test
    void test() {
        Invocation<CommandSender> invocation = invocation("test", "-4", "64", "-4");
        RequirementResult<Optional<Location>> result = parsers.parse(invocation, ARGUMENT, RawInput.of("-4", "64", "-4"))
            .await();

        assertTrue(result.isSuccessful());
        assertThat(result.getSuccess())
            .hasValue(new Location(null, -4, 64, -4));
    }

    @Test
    void testRange() {
        Parser<CommandSender, Optional<Location>> parser = parsers.getParser(ARGUMENT);
        Range range = parser.getRange(ARGUMENT);

        assertThat(range.getMin())
            .isEqualTo(0);
        assertThat(range.getMax())
            .isEqualTo(3);
    }

    @Test
    void testFail() {
        Invocation<CommandSender> invocation = invocation("test", "pos1");
        RequirementResult<Optional<Location>> result = parsers.parse(invocation, ARGUMENT, RawInput.of("pos1"))
            .await();

        assertTrue(result.isFailed());
    }


}
