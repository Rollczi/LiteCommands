package dev.rollczi.litecommands.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestSender {

    private final List<String> messages = new ArrayList<>();

    public void sendMessage(String message) {
        this.messages.add(message);
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

}
