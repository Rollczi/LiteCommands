package dev.rollczi.litecommands.bukkit.argument;

import org.bukkit.Location;

public enum LocationAxis {
    X, Y, Z;

    public static final int SIZE = values().length;

    public double getValue(Location location) {
        switch (this) {
            case X:
                return location.getX();
            case Y:
                return location.getY();
            case Z:
                return location.getZ();
        }

        throw new UnsupportedOperationException("Unknown axis: " + this.name());
    }

    public static LocationAxis at(int index) {
        for (LocationAxis axis : values()) {
            if (axis.ordinal() == index) {
                return axis;
            }
        }

        throw new IllegalArgumentException("Unknown axis index: " + index);
    }

}
