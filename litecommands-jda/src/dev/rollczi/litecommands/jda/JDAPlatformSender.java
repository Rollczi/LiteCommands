package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.identifier.Identifier;
import dev.rollczi.litecommands.platform.AbstractPlatformSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

class JDAPlatformSender extends AbstractPlatformSender {

    private final User user;
    private final @Nullable Member member;

    JDAPlatformSender(User user, @Nullable Member member) {
        this.user = user;
        this.member = member;
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
    @Deprecated
    public boolean hasPermission(String permission) {
        if (member == null) {
            return false;
        }

        Permission discordPermission = Permission.valueOf(permission.toLowerCase(Locale.ROOT));

        return member.hasPermission(discordPermission);
    }

}
