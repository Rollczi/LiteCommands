package dev.rollczi.litecommands.folia;

import dev.rollczi.litecommands.command.executor.event.CommandPreExecutionEvent;
import dev.rollczi.litecommands.event.EventListener;
import dev.rollczi.litecommands.event.Subscriber;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

class FoliaSchedulerExecutionController implements EventListener {

    @Subscriber
    void onExecute(CommandPreExecutionEvent<CommandSender> event) {
        CommandSender sender = event.getInvocation().sender();
        if (!(sender instanceof Entity entity)) {
            return;
        }

        event.setSchedulerType(new FoliaSchedulerType(entity.getLocation()));
    }

}
