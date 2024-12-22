package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.platform.PlatformSettings;
import org.jetbrains.annotations.ApiStatus;

public class LiteFabricSettings implements PlatformSettings {

    private String inputInspectionDisplay = "[...]";

    String getInputInspectionDisplay() {
        return this.inputInspectionDisplay;
    }

    /**
     * The name of the brigadier argument used to inspection, default is "[...]".
     * Inspection sometimes displays while the player is typing the command.
     * LiteCommands don't support brigadier suggestions, but we must provide a name for the default input argument.
     */
    @ApiStatus.Experimental
    public LiteFabricSettings inputInspectionDisplay(String name) {
        this.inputInspectionDisplay = name;
        return this;
    }

}
