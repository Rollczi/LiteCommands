package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.command.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;

import java.util.ArrayList;
import java.util.List;

public class CommandValidatorService<SENDER> {

    private final List<CommandValidator<SENDER>> commandValidators = new ArrayList<>();

    public void register(CommandValidator<SENDER> commandValidator) {
        this.commandValidators.add(commandValidator);
    }

    public CommandValidatorResult validate(Invocation<SENDER> invocation, CommandRoute<SENDER> commandRoute, CommandExecutor<SENDER> commandExecutor) {
        boolean invalid = false;

        for (CommandValidator<SENDER> commandValidator : this.commandValidators) {
            CommandValidatorResult result = commandValidator.validate(invocation, commandRoute, commandExecutor);

            if (result.isInvalid()) {
                if (result.canBeIgnored() || result.hasInvalidResult()) {
                    return result;
                }

                invalid = true;
            }
        }

        return invalid ? CommandValidatorResult.invalid(false) : CommandValidatorResult.valid();
    }

}
