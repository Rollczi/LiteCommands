package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.argument.joiner.JoinerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

class KyoriComponentJoinerArgument extends JoinerArgument<CommandSource, Component> {

    static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
        .postProcessor(new LegacyProcessor())
        .build();

    KyoriComponentJoinerArgument() {
        super(MINI_MESSAGE::deserialize);
    }

}
