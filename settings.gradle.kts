rootProject.name = "LiteCommands"

include(":litecommands-core")
include(":litecommands-velocity")
include(":litecommands-bukkit")
include(":litecommands-bungee")
include("examples:bukkit")
findProject(":examples:bukkit")?.name = "bukkit"
