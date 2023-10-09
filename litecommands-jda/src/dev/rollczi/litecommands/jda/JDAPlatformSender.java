package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import dev.rollczi.litecommands.platform.PlatformSender;
import net.dv8tion.jda.api.entities.User;

class JDAPlatformSender extends AbstractPlatformSender {

    private final User user;

    JDAPlatformSender(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public Identifier getIdentifier() {
        return Identifier.of(user.getId(), user.getIdLong());
    }

    @Override
    public boolean hasPermission(String permission) {
        return false; // TODO: Move permission check to specific platform
    }

}
