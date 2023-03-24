package dev.rollczi.litecommands.adventure;

import net.kyori.adventure.audience.Audience;

public interface AdventureAudienceProvider<SENDER> {

    Audience sender(SENDER commandSender);

    static <SENDER> AdventureAudienceProvider<SENDER> simple() {
        return commandSender -> {
           if (commandSender instanceof Audience) {
               return (Audience) commandSender;
           }

           throw new IllegalArgumentException("Unsupported command sender type: " + commandSender.getClass().getName());
        };
    }

}
