package dev.rollczi.litecommands.folia;

import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import org.bukkit.Location;

class FoliaSchedulerPoll extends SchedulerPoll {

    private final Location location;

    public FoliaSchedulerPoll(Location location) {
        super(location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ());
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

}
