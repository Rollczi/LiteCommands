package dev.rollczi.litecommands.bukkit.tabcomplete;

import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.reflect.ReflectUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Tab completer for Paper 1.12 - current
 */
class TabCompletePaperAsync extends AbstractAsyncTabComplete implements Listener {

    public TabCompletePaperAsync(Plugin plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvent(PaperAsyncTabCompleteEvent.TAB_COMPLETE_CLASS, this, EventPriority.HIGHEST, (listener, event) -> executeListener(event), plugin);
    }

    private void executeListener(Event event) {
        PaperAsyncTabCompleteEvent tabCompleteEvent = new PaperAsyncTabCompleteEvent(event);
            List<String> result = this.callListener(tabCompleteEvent.getSender(), tabCompleteEvent.getBuffer());

        if (result == null) {
            return;
        }

         tabCompleteEvent.setCompletions(result);
    }

    @Override
    public void close() {
        super.close();
        HandlerList.unregisterAll(this);
    }

    static class PaperAsyncTabCompleteEvent {

        private static final String TAB_COMPLETE_CLASS_NAME = "com.destroystokyo.paper.event.server.AsyncTabCompleteEvent";

        static final Class<? extends Event> TAB_COMPLETE_CLASS;
        static final Method GET_COMMAND_SENDER_METHOD;
        static final Method GET_BUFFER_METHOD;
        static final Method SET_COMPLETIONS_METHOD;

        static {
            TAB_COMPLETE_CLASS = ReflectUtil.getClass(TAB_COMPLETE_CLASS_NAME);
            GET_COMMAND_SENDER_METHOD = ReflectUtil.getMethod(TAB_COMPLETE_CLASS, "getSender");
            GET_BUFFER_METHOD = ReflectUtil.getMethod(TAB_COMPLETE_CLASS, "getBuffer");
            SET_COMPLETIONS_METHOD = ReflectUtil.getMethod(TAB_COMPLETE_CLASS, "setCompletions", List.class);
        }

        private final Object event;

        PaperAsyncTabCompleteEvent(Object event) {
            if (!ReflectUtil.instanceOf(event, TAB_COMPLETE_CLASS)) {
                throw new LiteCommandsException("Event is not instance of " + TAB_COMPLETE_CLASS_NAME);
            }

            this.event = event;
        }

        public CommandSender getSender() {
            return ReflectUtil.invokeMethod(GET_COMMAND_SENDER_METHOD, event);
        }

        public String getBuffer() {
            return ReflectUtil.invokeMethod(GET_BUFFER_METHOD, event);
        }

        public void setCompletions(List<String> completions) {
            ReflectUtil.invokeMethod(SET_COMPLETIONS_METHOD, event, completions);
        }

    }
}
