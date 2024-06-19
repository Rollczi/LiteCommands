package dev.rollczi.example.bukkit.command.currency;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class is a simple service that stores currency for players. (Used for demonstration /currency command)
 * {@link CurrencyCommand} and {@link CurrencyBalanceCommand} are using this service.
 */
public class CurrencyService {

    private final Map<UUID, Integer> currency = new HashMap<>();

    public void setCurrency(UUID uuid, int amount) {
        currency.put(uuid, amount);
    }

    public void addCurrency(UUID uuid, int amount) {
        currency.put(uuid, currency.getOrDefault(uuid, 0) + amount);
    }

    public void removeCurrency(UUID uuid, int amount) {
        currency.put(uuid, currency.getOrDefault(uuid, 0) - amount);
    }

    public int getCurrency(UUID uuid) {
        return currency.getOrDefault(uuid, 0);
    }

}
