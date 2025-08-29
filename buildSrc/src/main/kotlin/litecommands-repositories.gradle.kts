plugins {
    id("java-library")
}

repositories {
    mavenCentral()

    maven("https://maven.fabricmc.net/") // fabric
    maven("https://repo.papermc.io/repository/maven-public/") // paper, adventure, velocity
    maven("https://repo.opencollab.dev/maven-snapshots") // nukkit
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // spigot
    maven("https://jitpack.io/") // minestom
    maven("https://repo.panda-lang.org/releases/") // expressible
    maven("https://repo.spongepowered.org/repository/maven-public/") // sponge
}