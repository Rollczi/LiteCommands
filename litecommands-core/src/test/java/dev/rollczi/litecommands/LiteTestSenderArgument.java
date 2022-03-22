package dev.rollczi.litecommands;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.SingleArgumentHandler;
import dev.rollczi.litecommands.valid.ValidationCommandException;

import java.util.Arrays;
import java.util.List;

@ArgumentName("test_sender")
public class LiteTestSenderArgument implements SingleArgumentHandler<LiteTestSender> {

    @Override
    public LiteTestSender parse(LiteInvocation invocation, String argument) throws ValidationCommandException {
        return new LiteTestSender();
    }

    @Override
    public List<String> tabulation(LiteInvocation invocation, String command, String[] args) {
        return Arrays.asList("test1", "test2", "test3");
    }

}
