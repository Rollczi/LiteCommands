package dev.rollczi.litecommands;

import dev.rollczi.litecommands.platform.LiteSender;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LiteTestSender implements LiteSender {

    private final Set<String> permissions = new HashSet<>();
    private boolean ignoreMessages = false;

    public LiteTestSender permission(String... permissions) {
        this.permissions.addAll(Arrays.asList(permissions));
        return this;
    }

    public LiteTestSender ignoreMessages(boolean ignoreMessages) {
        this.ignoreMessages = ignoreMessages;
        return this;
    }

    @Override
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    @Override
    public void sendMessage(String message) {
        if (ignoreMessages) {
            return;
        }

        throw new UnsupportedOperationException("Can't send message on TestSender! content: " + message);
    }

    @Override
    public Object getSender() {
        throw new UnsupportedOperationException("Can't get origin sender of TestSender!");
    }

}
