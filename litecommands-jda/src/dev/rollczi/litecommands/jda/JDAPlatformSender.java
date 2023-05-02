package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.platform.PlatformSender;
import net.dv8tion.jda.api.entities.User;

class JDAPlatformSender implements PlatformSender {

    private final User user;

    JDAPlatformSender(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return false; // TODO: Move permission check to specific platform
    }

}
