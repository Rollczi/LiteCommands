package dev.rollczi.litecommands.folia;

import dev.rollczi.litecommands.scheduler.SchedulerType;
import org.bukkit.Location;

class FoliaSchedulerType extends SchedulerType {

    private final Location location;

    public FoliaSchedulerType(Location location) {
        super(location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ());
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

}
