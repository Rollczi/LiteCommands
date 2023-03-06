package dev.rollczi.litecommands.test;

import java.util.ArrayList;
import java.util.List;

public class TestHandle {

    private final List<String> registeredMessages = new ArrayList<>();

    public void message(String message) {
        registeredMessages.add(message);
    }

    public boolean containsMessage(String message) {
        return registeredMessages.contains(message);
    }

}
