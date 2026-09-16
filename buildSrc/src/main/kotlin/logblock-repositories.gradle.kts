plugins {
    `java-library`
}

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://repo.eternalcode.pl/releases")
    maven("https://repo.eternalcode.pl/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://jitpack.io/")
}