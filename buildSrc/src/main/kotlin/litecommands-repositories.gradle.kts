plugins {
    id("java-library")
}

repositories {
    mavenCentral()

    maven { url = uri("https://papermc.io/repo/repository/maven-public/") } // paper, adventure, velocity
    maven { url = uri("https://repo.opencollab.dev/maven-snapshots") } // nukkit
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") } // spigot
    maven { url = uri("https://jitpack.io/") } // minestom
    maven { url = uri("https://repo.panda-lang.org/releases/") } // expressible
}