plugins {
    id("java-library")
}

repositories {
    mavenCentral()

    maven("https://repo.dmulloy2.net/repository/public/") // protocol lib
    maven("https://papermc.io/repo/repository/maven-public/") // paper, adventure, velocity
    maven("https://repo.opencollab.dev/maven-snapshots") // nukkit
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // spigot
    maven("https://jitpack.io/") // minestom
    maven("https://repo.panda-lang.org/releases/") // expressible
    maven("https://repo.spongepowered.org/repository/maven-public/") // sponge
}