package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.extension.annotations.LiteAnnotationsProcessorExtension;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.jda.permission.DiscordMissingPermissions;
import dev.rollczi.litecommands.jda.permission.DiscordMissingPermissionsHandler;
import dev.rollczi.litecommands.jda.permission.DiscordPermissionValidator;
import dev.rollczi.litecommands.jda.permission.DiscordPermissionAnnotationProcessor;
import dev.rollczi.litecommands.jda.visibility.VisibilityAnnotationProcessor;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.RestAction;

public final class LiteJDAFactory {

    private LiteJDAFactory() {
    }

    public static LiteCommandsBuilder<User, LiteJDASettings, ?> builder(JDA jda) {
        JDAPlatform platform = new JDAPlatform(new LiteJDASettings(), jda);

        return LiteCommandsFactory.builder(User.class, platform).selfProcessor((builder, internal) -> builder
            .settings(settings -> settings.translator(createTranslator(internal.getWrapperRegistry())))
            .bind(JDA.class, () -> jda)
            .result(String.class, new StringHandler())
            .result(RestAction.class, new RestActionHandler())
            .result(MessageEmbed.class, new MessageEmbedHandler())

            .context(Guild.class, invocation -> from(invocation, Guild.class))
            .context(MessageChannelUnion.class, invocation -> from(invocation, MessageChannelUnion.class))
            .context(Member.class, invocation -> from(invocation, Member.class))
            .context(SlashCommandInteractionEvent.class, invocation -> from(invocation, SlashCommandInteractionEvent.class))

            .validator(Scope.global(), new DiscordPermissionValidator())
            .result(DiscordMissingPermissions.class, new DiscordMissingPermissionsHandler<>(internal.getMessageRegistry()))
            .extension(new LiteAnnotationsProcessorExtension<>(), extension -> extension
                .processor(new DiscordPermissionAnnotationProcessor<>())
                .processor(new VisibilityAnnotationProcessor<>())
            )
        );
    }

    private static JDACommandTranslator createTranslator(WrapperRegistry wrapperRegistry) {
        return new JDACommandTranslator(wrapperRegistry)
            .type(String.class, OptionType.STRING, option -> option.getAsString())
            .type(Long.class, OptionType.INTEGER, option -> option.getAsLong())
            .type(long.class, OptionType.INTEGER, option -> option.getAsLong())
            .type(Integer.class, OptionType.INTEGER, option -> option.getAsInt())
            .type(int.class, OptionType.INTEGER, option -> option.getAsInt())
            .type(Boolean.class, OptionType.BOOLEAN, option -> option.getAsBoolean())
            .type(boolean.class, OptionType.BOOLEAN, option -> option.getAsBoolean())
            .type(User.class, OptionType.USER, option -> option.getAsUser())
            .type(double.class, OptionType.NUMBER, option -> option.getAsDouble())
            .type(Attachment.class, OptionType.ATTACHMENT, option -> option.getAsAttachment())
            .type(Role.class, OptionType.ROLE, option -> option.getAsRole())
            .type(IMentionable.class, OptionType.MENTIONABLE, option -> option.getAsMentionable())
            .type(Channel.class, OptionType.CHANNEL, option -> option.getAsChannel())

            .typeOverlay(Float.class, OptionType.NUMBER, option -> option.getAsString())
            .typeOverlay(float.class, OptionType.NUMBER, option -> option.getAsString())
            .typeOverlay(Member.class, OptionType.USER, option -> option.getAsString()) // TODO: Add raw member parer
            ;
    }

    private static <T> ContextResult<T> from(Invocation<User> invocation, Class<T> type) {
        return invocation.context().get(type)
            .map(t -> ContextResult.ok(() -> t))
            .orElseGet(() -> ContextResult.error(type.getSimpleName() + " is not present"));
    }
}
