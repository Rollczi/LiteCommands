package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
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
        return Identifier.of(user.getIdLong());
    }

    @Override
    public Object getHandle() {
        return this.user;
    }

}
