package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.invocation.Invocation;
import net.kyori.adventure.audience.Audience;

public interface AdventureAudienceProvider<SENDER> {

    Audience sender(Invocation<SENDER> invocation);

    static <SENDER> AdventureAudienceProvider<SENDER> simple() {
        return invocation -> {
            SENDER sender = invocation.sender();

            if (sender instanceof Audience) {
                return (Audience) sender;
            }

            throw new IllegalArgumentException("Unsupported command sender type: " + sender.getClass().getName());
        };
    }

}
