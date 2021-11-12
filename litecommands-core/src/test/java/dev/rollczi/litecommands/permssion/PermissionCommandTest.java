package dev.rollczi.litecommands.permssion;

import dev.rollczi.litecommands.annotations.Permission;
import dev.rollczi.litecommands.annotations.PermissionExclude;
import dev.rollczi.litecommands.annotations.Section;
import dev.rollczi.litecommands.annotations.Execute;

@Section(route = "main")
@Permission("dev.rollczi.main")
public class PermissionCommandTest {

    @Execute
    public void execute() {}

    @Section(route = "inner")
    @Permission("dev.rollczi.main.inner")
    public static class InnerSection {

        @Execute(route = "all")
        public void execute() {}

        @Execute(route = "without")
        @PermissionExclude("dev.rollczi.main")
        @PermissionExclude("dev.rollczi.main.inner")
        public void executeNoPerm() {}

        @Execute(route = "inner")
        @PermissionExclude("dev.rollczi.main")
        public void executeWithOutMainPerm() {}

        @Execute(route = "main")
        @PermissionExclude("dev.rollczi.main.inner")
        public void executeWithOutInnerPerm() {}

    }

}
