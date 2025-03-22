package dev.rollczi.litecommands.telegrambots;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import org.telegram.telegrambots.meta.api.objects.User;

class TelegramBotsSender extends AbstractPlatformSender {

    private final User handle;
    private final LiteTelegramBotsSettings liteTelegramBotsSettings;

    public TelegramBotsSender(User handle, LiteTelegramBotsSettings liteTelegramBotsSettings) {
        this.handle = handle;
        this.liteTelegramBotsSettings = liteTelegramBotsSettings;
    }

    @Override
    public String getName() {
        return this.handle.getUserName();
    }

    @Override
    public String getDisplayName() {
        return this.handle.getFirstName() + " " + this.handle.getLastName();
    }

    @Override
    public Identifier getIdentifier() {
        return Identifier.of(handle.getId());
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.liteTelegramBotsSettings.getPermissionChecker().test(handle, permission);
    }

}
