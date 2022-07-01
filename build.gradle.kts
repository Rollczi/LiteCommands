
val coreArtifact by extra("core")
val velocityArtifact by extra("velocity")
val bukkitArtifact by extra("bukkit")
val bungeeArtifact by extra("bungee")

plugins {
    id("idea")
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "1.7.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

allprojects {
    group = "dev.rollczi.litecommands"
    version = "2.1.1"

    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")

    java {
        withSourcesJar()
    }
}

subprojects {
    repositories {
        mavenCentral()

        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
        maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
        maven { url = uri("https://repo.panda-lang.org/releases/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    publishing {
        repositories {
            mavenLocal()
//
//            maven {
//                name = "panda-repository"
//                url = uri("https://repo.panda-lang.org/releases")
//                credentials {
//                    username = properties["panda_user_litecommands"] as String
//                    password = properties["panda_pass_litecommands"] as String
//                }
//            }
//
//            maven {
//                name = "eternalcode-repository"
//                url = uri("https://repo.eternalcode.pl/releases")
//                credentials {
//                    username = properties["lucky_user_litecommands"] as String
//                    password = properties["lucky_pass_litecommands"] as String
//                }
//            }
//            maven {
//                name = "mine-repository"
//                url = uri("https://repository.minecodes.pl/releases")
//                credentials {
//                    username = properties["mine_user"] as String
//                    password = properties["mine_pass"] as String
//                }
//            }
        }
    }
}

idea {
    project {
        jdkName = "8"
    }
}