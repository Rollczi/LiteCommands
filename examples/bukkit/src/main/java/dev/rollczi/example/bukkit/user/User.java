package dev.rollczi.example.bukkit.user;

import java.util.UUID;

public class User {

    private final UUID uuid;
    private final String name;

    User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    User(String name) {
        this(UUID.randomUUID(), name);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

}
