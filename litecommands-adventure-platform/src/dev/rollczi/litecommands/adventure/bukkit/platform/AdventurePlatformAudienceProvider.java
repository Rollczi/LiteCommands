package dev.rollczi.litecommands.adventure.bukkit.platform;

import dev.rollczi.litecommands.adventure.AdventureAudienceProvider;
import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.platform.PlatformSender;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;

import java.util.Optional;
import java.util.UUID;

class AdventurePlatformAudienceProvider<SENDER> implements AdventureAudienceProvider<SENDER> {

    private final AudienceProvider audienceProvider;

    public AdventurePlatformAudienceProvider(AudienceProvider audienceProvider) {
        this.audienceProvider = audienceProvider;
    }

    @Override
    public Audience sender(Invocation<SENDER> invocation) {
        SENDER sender = invocation.sender();

        if (sender instanceof Audience) {
            return (Audience) sender;
        }

        PlatformSender platformSender = invocation.platformSender();
        Identifier identifier = platformSender.getIdentifier();

        if (identifier.equals(Identifier.CONSOLE)) {
            return audienceProvider.console();
        }

        Optional<UUID> uuidOptional = identifier.getIdentifier(UUID.class);

        if (uuidOptional.isPresent()) {
            return audienceProvider.player(uuidOptional.get());
        }

        throw new IllegalArgumentException("Unsupported command sender type: " + sender.getClass().getName() + " or missing identifier");
    }

}
