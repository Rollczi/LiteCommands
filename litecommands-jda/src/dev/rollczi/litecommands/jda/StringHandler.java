package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import panda.std.Option;

class StringHandler implements ResultHandler<User, String> {

    @Override
    public void handle(Invocation<User> invocation, String result, ResultHandlerChain<User> chain) {
        Option<SlashCommandInteractionEvent> eventOption = invocation.context().get(SlashCommandInteractionEvent.class);

        if (eventOption.isEmpty()) {
            invocation.sender().openPrivateChannel().queue(channel -> channel.sendMessage(result).queue());
            return;
        }

        SlashCommandInteractionEvent event = eventOption.get();
        event.reply(result)
            .setEphemeral(true)
            .queue();
    }

}
