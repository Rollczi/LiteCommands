package dev.rollczi.example.bukkit.user;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

// This class is used to simulate a service that fetches a user from a database.
// DON'T COPY THIS CODE.
public class UserService {

    private final Map<String, User> usersCache = new HashMap<>();

    public UserService() {
        usersCache.put("rollczi", new User("rollczi"));
        usersCache.put("example", new User("example"));
        usersCache.put("Notch", new User("Notch"));
    }

    public CompletableFuture<User> getUser(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (usersCache.containsKey(name)) {
                return usersCache.get(name);
            }

            return null;
        });
    }

    public Collection<User> getCachedUsers() {
        return Collections.unmodifiableCollection(usersCache.values());
    }

}
