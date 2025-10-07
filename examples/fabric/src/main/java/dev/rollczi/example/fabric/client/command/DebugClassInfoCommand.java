package dev.rollczi.example.fabric.client.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;

@Command(name = "debug-server test")
public class DebugClassInfoCommand {

    @Execute(name = "gts item_history")
    void getGtsItemHistoryInfo() {
    }

    @Execute(name = "gts item_history")
    void getGtsItemHistoryInfo(@Arg String itemHistoryKey) {
    }

    @Execute(name = "gts item")
    void getGtsItemInfo() {
    }

    @Execute(name = "gts pokemon")
    void getGtsPokemonInfo() {
    }

    @Execute(name = "gts")
    void getGtsInfo() {

    }

    @Execute(name = "gyms")
    void getGyms() {

    }

    @Execute(name = "gyms")
    void getGym(@Arg GymType gymType) {

    }

    @Execute(name = "gyms battle")
    void getActiveBattles() {

    }

    enum GymType {
        NORMAL,
        RAID,
        TEAM_ROCKET
    }

}