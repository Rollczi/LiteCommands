package dev.rollczi.example.bukkit.command.currency;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "currency")
public class CurrencyCommand {

    private final CurrencyService currencyService;

    public CurrencyCommand(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Execute(name = "give")
    void give(@Context CommandSender sender, @Arg Player player, @Arg int amount) {
        currencyService.addCurrency(player.getUniqueId(), amount);

        sender.sendMessage("Given " + amount + " to " + player.getName());
        player.sendMessage("You have received " + amount + " from " + sender.getName());
    }

    @Execute(name = "take")
    void take(@Context CommandSender sender, @Arg Player player, @Arg int amount) {
        currencyService.removeCurrency(player.getUniqueId(), amount);

        sender.sendMessage("Taken " + amount + " from " + player.getName());
        player.sendMessage("Your balance has been reduced by " + amount + " by " + sender.getName());
    }

    @Execute(name = "set")
    void set(@Context CommandSender sender, @Arg Player player, @Arg int amount) {
        currencyService.setCurrency(player.getUniqueId(), amount);

        sender.sendMessage("Set " + amount + " to " + player.getName());
        player.sendMessage("Your balance has been set to " + amount + " by " + sender.getName());
    }

    @Execute(name = "take")
    void reset(@Context CommandSender sender, @Arg Player player) {
        currencyService.setCurrency(player.getUniqueId(), 0);

        sender.sendMessage("Reset " + player.getName());
        player.sendMessage("Your balance has been reset by " + sender.getName());
    }

    @Execute(name = "pay")
    void pay(@Context Player sender, @Arg Player player, @Arg int amount) {
        currencyService.addCurrency(player.getUniqueId(), amount);
        currencyService.removeCurrency(sender.getUniqueId(), amount);

        sender.sendMessage("Paid " + amount + " to " + player.getName());
        player.sendMessage("You have received " + amount + " from " + sender.getName());
    }

}
