package dev.rollczi.example.bukkit.command.currency;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "currency balance")
public class CurrencyBalanceCommand {

    private final CurrencyService currencyService;

    public CurrencyBalanceCommand(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Execute
    void balance(@Context Player sender) {
        sender.sendMessage("Your balance is " + currencyService.getCurrency(sender.getUniqueId()));
    }

    @Execute
    void balance(@Context CommandSender sender, @Arg Player player) {
        sender.sendMessage(player.getName() + "'s balance is " + currencyService.getCurrency(player.getUniqueId()));
    }

}
