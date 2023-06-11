package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message.Attachment;
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
        JDACommandTranslator translator = new JDACommandTranslator()
            .type(String.class,       OptionType.STRING,      option -> option.getAsString())
            .type(Long.class,         OptionType.INTEGER,     option -> option.getAsLong())
            .type(long.class,         OptionType.INTEGER,     option -> option.getAsLong())
            .type(Integer.class,      OptionType.INTEGER,     option -> option.getAsInt())
            .type(int.class,          OptionType.INTEGER,     option -> option.getAsInt())
            .type(Boolean.class,      OptionType.BOOLEAN,     option -> option.getAsBoolean())
            .type(boolean.class,      OptionType.BOOLEAN,     option -> option.getAsBoolean())
            .type(User.class,         OptionType.USER,        option -> option.getAsUser())
            .type(double.class,       OptionType.NUMBER,      option -> option.getAsDouble())
            .type(Attachment.class,   OptionType.ATTACHMENT,  option -> option.getAsAttachment())
            .type(Role.class,         OptionType.ROLE,        option -> option.getAsRole())
            .type(IMentionable.class, OptionType.MENTIONABLE, option -> option.getAsMentionable())
            .type(Channel.class,      OptionType.CHANNEL,     option -> option.getAsChannel())

            .typeOverlay(Float.class,  OptionType.NUMBER, option -> option.getAsString())
            .typeOverlay(float.class,  OptionType.NUMBER, option -> option.getAsString())
            .typeOverlay(Member.class, OptionType.USER,   option -> option.getAsString()) // TODO: Add raw member parer
            ;

        return LiteCommandsFactory.builder(User.class, new JDAPlatform(new LiteJDASettings(), jda, translator))
            .bind(JDA.class, () -> jda)
            .result(String.class, new StringHandler())
            .result(RestAction.class, new RestActionHandler())

            .context(Guild.class, invocation -> invocation.context().get(Guild.class).toResult("Guild is not present"))
            .context(MessageChannelUnion.class, invocation -> invocation.context().get(MessageChannelUnion.class).toResult("Channel is not present"))
            .context(Member.class, invocation -> invocation.context().get(Member.class).toResult("Member is not present"))
            .context(SlashCommandInteractionEvent.class, invocation -> invocation.context().get(SlashCommandInteractionEvent.class).toResult("SlashCommandInteractionEvent is not present"))

            ;
    }

}
