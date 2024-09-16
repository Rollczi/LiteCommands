package dev.rollczi.litecommands.jda.permission;

import dev.rollczi.litecommands.flow.Flow;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.validator.Validator;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DiscordPermissionValidator implements Validator<User> {

    @Override
    public Flow validate(Invocation<User> invocation, MetaHolder metaHolder) {
        List<Permission> required = metaHolder.metaCollector().collect(DiscordPermission.META_KEY).stream()
            .flatMap(permissions -> permissions.stream())
            .collect(Collectors.toList());

        if (required.isEmpty()) {
            return Flow.continueFlow();
        }

        User sender = invocation.sender();
        CommandInteractionPayload interactionPayload = invocation.context().get(CommandInteractionPayload.class)
            .orElseThrow(() -> new IllegalStateException("CommandInteractionPayload is not present in context"));

        Guild guild = interactionPayload.getGuild();
        Member member = interactionPayload.getMember();

        if (guild == null || member == null) {
            return Flow.terminateFlow(new IllegalStateException("@DiscordPermission is not supported in DM"));
        }

        if (sender.getIdLong() == guild.getOwnerIdLong()) {
            return Flow.continueFlow();
        }

        List<Permission> missingPermissions = invocation.context().get(GuildChannel.class)
            .map(channel -> getMissing(member, channel, required))
            .orElseGet(() -> getMissing(member, required));

        if (missingPermissions.isEmpty()) {
            return Flow.continueFlow();
        }

        return Flow.terminateFlow(new DiscordMissingPermissions(required, missingPermissions));
    }

    private List<Permission> getMissing(Member member, List<Permission> required) {
        List<Permission> missing = new ArrayList<>();

        for (Permission permission : required) {
            if (!member.hasPermission(permission)) {
                missing.add(permission);
            }
        }

        return missing;
    }

    private List<Permission> getMissing(Member member, GuildChannel channel, List<Permission> required) {
        List<Permission> missing = new ArrayList<>();

        for (Permission permission : required) {
            if (!member.hasPermission(channel, permission)) {
                missing.add(permission);
            }
        }

        return missing;
    }

}
