import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService

plugins {
    id("xyz.jpenilla.run-paper")
}

tasks {
    val javaToolchains = extensions.getByType<JavaToolchainService>()

    runServer {
        javaLauncher.set(javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(25))
        })

        minecraftVersion("26.2")
        downloadPlugins.modrinth("LuckPerms", "v5.5.53-bukkit")
    }
}