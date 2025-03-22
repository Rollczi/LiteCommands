package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;

public final class TestUtil {

    private TestUtil() {
    }

    public static Invocation<TestSender> invocation(String command, String... args) {
        return new Invocation<>(new TestPlatformSender(new TestSender()), command, command, ParseableInput.raw(args));
    }

    public static <SENDER> Invocation<SENDER> invocation(SENDER sender, String command, String... args) {
        return new Invocation<>(new WrapperPlatformSender(sender), command, command, ParseableInput.raw(args));
    }

    private static class WrapperPlatformSender extends AbstractPlatformSender  {
        private final Object sender;

        public WrapperPlatformSender(Object sender) {
            this.sender = sender;
        }

        @Override
        public Object getHandle() {
            return sender;
        }

        @Override
        public String getName() {
            return sender.getClass().getSimpleName();
        }

        @Override
        public Identifier getIdentifier() {
            return Identifier.of(sender);
        }
    }

}
