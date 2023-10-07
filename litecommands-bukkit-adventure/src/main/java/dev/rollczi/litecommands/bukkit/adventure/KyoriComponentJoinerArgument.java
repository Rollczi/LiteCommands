package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.argument.joiner.JoinerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.command.CommandSender;

class KyoriComponentJoinerArgument extends JoinerArgument<CommandSender, Component> {

    KyoriComponentJoinerArgument(ComponentSerializer<Component, ?, String> kyoriComponentSerializer) {
        super(kyoriComponentSerializer::deserialize);
    }

}
