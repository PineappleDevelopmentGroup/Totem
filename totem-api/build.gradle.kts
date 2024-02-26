plugins {
    id("java")
    id("maven-publish")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("org.jetbrains:annotations:24.0.0")
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT") { isChanging = true }
    compileOnly("sh.miles:Pineapple:1.0.0-SNAPSHOT") { isChanging = true }
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("Maven") {
            this.artifact(tasks.javadoc)
        }
    }

    repositories {
        maven("https://maven.miles.sh/libraries") {
            credentials {
                this.username = System.getenv("PINEAPPLE_REPOSILITE_USERNAME")
                this.password = System.getenv("PINEAPPLE_REPOSILITE_PASSWORD")
            }
        }
    }
}
