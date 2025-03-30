package dev.rollczi.example.telegrambots.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Sender;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.telegram.telegrambots.meta.api.objects.User;

@Command(name = "tell")
public class TellCommand {

    @Execute
    String executeTell(@Sender User user) {
        return user.getFirstName() + " " + user.getLastName() + " told me that you are a good person!";
    }

}
