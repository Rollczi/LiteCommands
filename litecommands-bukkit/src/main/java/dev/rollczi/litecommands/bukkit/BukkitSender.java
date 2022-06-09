package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.meta.MetaData;
import dev.rollczi.litecommands.platform.LiteSender;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;

class BukkitSender implements LiteSender {

    private final CommandSender handle;

    public BukkitSender(CommandSender handle) {
        this.handle = handle;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.handle.hasPermission(permission);
    }

    @Override
    public Object getHandle() {
        return this.handle;
    }
}
