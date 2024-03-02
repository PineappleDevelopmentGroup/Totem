plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT") { isChanging = true }
    implementation("sh.miles:Pineapple:1.0.0-SNAPSHOT") { isChanging = true }
    bukkitLibrary(kotlin("stdlib"))
    implementation(project(":totem-api"))
    implementation("org.bstats:bstats-bukkit:3.0.2")
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "17"
}

tasks.test {
    useJUnitPlatform()
}

bukkit {
    name = rootProject.name
    version = rootProject.version.toString()
    main = "sh.miles.${rootProject.name.lowercase()}.${rootProject.name}Plugin"
    apiVersion = "1.20" // LATEST
}

kotlin {
    jvmToolchain(17)
}
