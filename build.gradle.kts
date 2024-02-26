plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val debugLibraries = true

// This is only added here as a convenience
dependencies {
    implementation(project(":totem-plugin"))
}

tasks.shadowJar {
    this.archiveClassifier = ""
    this.archiveVersion = ""
    archiveFileName = "${project.name}-${project.version}.jar"

    val packageName = "${project.group}.${project.name.lowercase()}"
    this.relocate("sh.miles.pineapple", "$packageName.libs.pineapple")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

subprojects {
    if (debugLibraries) {
        configurations.all {
            resolutionStrategy.cacheChangingModulesFor(0, "seconds")
        }
    }
}
