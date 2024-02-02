package dev.rollczi.example.bukkit.validator;

import dev.rollczi.litecommands.annotations.validator.requirment.AnnotatedValidator;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.validator.ValidatorResult;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IsNotOpValidator implements AnnotatedValidator<CommandSender, Player, IsNotOp> {

    @Override
    public ValidatorResult validate(
        Invocation<CommandSender> invocation,
        CommandExecutor<CommandSender> executor,
        Requirement<Player> requirement,
        Player player,
        IsNotOp annotation
    ) {
        if (player.isOp()) {
            return ValidatorResult.invalid("Player " + player.getName() + " is server operator!");
        }

        return ValidatorResult.valid();
    }

}
