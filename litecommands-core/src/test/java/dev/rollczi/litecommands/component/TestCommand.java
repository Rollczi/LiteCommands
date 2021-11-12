package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.annotations.Permission;
import dev.rollczi.litecommands.annotations.PermissionExclude;
import dev.rollczi.litecommands.annotations.Required;
import dev.rollczi.litecommands.annotations.Section;
import dev.rollczi.litecommands.annotations.Execute;

@Section(route = "ac")
@Permission("dev.rollczi.ac")
public class TestCommand {

    public int executeAc = 0;
    public int executeHelp = 0;

    @Execute
    public void execute() {
        executeAc++;
    }

    @Execute(route = "help")
    public void executeHelp() {
        executeHelp++;
    }

    @Section(route = "manage")
    @Permission("dev.rollczi.ac.manage")
    public static class ManageCommand {

        public int executeHelp = 0;
        public int executeMove = 0;
        public int executeKick = 0;

        @Execute
        @PermissionExclude("dev.rollczi.ac.manage")
        public void executeHelp() {
            executeHelp++;
        }

        @Execute(route = "move")
        @Required(2)
        public void executeMove() {
            executeMove++;
        }


        @Execute(route = "kick")
        @Required(1)
        public void executeKick() {
            executeKick++;
        }

    }

}
