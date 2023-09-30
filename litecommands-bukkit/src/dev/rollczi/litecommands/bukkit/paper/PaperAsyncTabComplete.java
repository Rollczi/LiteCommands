package dev.rollczi.litecommands.bukkit.paper;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class PaperAsyncTabComplete {

    private static final String ASYNC_TAB_COMPLETE_EVENT_CLASS = "com.destroystokyo.paper.event.server.AsyncTabCompleteEvent";

    private final Plugin plugin;
    private final PluginManager pluginManager;
    private Class<? extends Event> tabCompleteClass;
    private Method getCommandSenderMethod;
    private Method getLabelMethod;
    private Method getArgsMethod;

    public PaperAsyncTabComplete(Plugin plugin) {
        this.plugin = plugin;
        this.pluginManager = plugin.getServer().getPluginManager();

        try {
            tabCompleteClass = (Class<? extends Event>) Class.forName(ASYNC_TAB_COMPLETE_EVENT_CLASS, true, PaperAsyncTabComplete.class.getClassLoader());
            getCommandSenderMethod = tabCompleteClass.getDeclaredMethod("getSender");
            getLabelMethod = tabCompleteClass.getDeclaredMethod("getBuffer");
            getArgsMethod = tabCompleteClass.getDeclaredMethod("getCompletions");
        }
        catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }
    }

    public boolean canRegister() {
        return tabCompleteClass != null;
    }

    public void registerListener(AyncTabCompleteListener asyncTabCompleteListener) {
        if (tabCompleteClass == null) {
            throw new IllegalStateException();
        }

        pluginManager.registerEvent(tabCompleteClass, asyncTabCompleteListener, EventPriority.HIGHEST, (listener, event) -> executeListener(listener, event), plugin);
    }

    private void executeListener(Listener listener, Event event) {
        if (!(listener instanceof AyncTabCompleteListener)) {
            throw new IllegalStateException();
        }

        if (!event.getClass().equals(tabCompleteClass)) {
            throw new IllegalStateException();
        }

        AyncTabCompleteListener tabCompleteListener = (AyncTabCompleteListener) listener;

        String label = getLabel(event);
        Logger logger = Logger.getLogger("test");
        logger.info("buffer: " + label);
        logger.info(Bukkit.getVersion());
        logger.info(Bukkit.getBukkitVersion());
        logger.info(String.valueOf(Bukkit.getUnsafe().getDataVersion()));

    }

    private String getLabel(Event event) {
        try {
            return (String) getLabelMethod.invoke(event);
        }
        catch (IllegalAccessException | InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }

}
