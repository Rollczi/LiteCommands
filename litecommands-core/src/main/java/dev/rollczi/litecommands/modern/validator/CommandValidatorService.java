package dev.rollczi.litecommands.modern.validator;

import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.invocation.Invocation;

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
