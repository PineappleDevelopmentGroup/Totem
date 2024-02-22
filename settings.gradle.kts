plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "Totem"

gradle.rootProject {
    this.version = "1.0.0-SNAPSHOT"
    this.group = "sh.miles"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://maven.miles.sh/libraries")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

include("totem-api")
include("totem-plugin")
