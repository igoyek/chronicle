import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    `chronicle-java`
    `chronicle-repositories`
    `chronicle-runserver`

    id("de.eldoria.plugin-yml.paper") version "0.9.0"
    id("com.gradleup.shadow")
}

dependencies {
    implementation(project(":chronicle-api"))

    compileOnly("io.papermc.paper:paper-api:${Versions.PAPER_API}")

    implementation("dev.rollczi:litecommands-bukkit:${Versions.LITE_COMMANDS}")

    implementation("eu.okaeri:okaeri-configs-serdes-commons:${Versions.OKAERI_CONFIGS_SERDES_COMMONS}")
    implementation("eu.okaeri:okaeri-configs-serdes-bukkit:${Versions.OKAERI_CONFIGS_SERDES_BUKKIT}")

    implementation("org.bstats:bstats-bukkit:${Versions.B_STATS_BUKKIT}")

    implementation("com.github.ben-manes.caffeine:caffeine:${Versions.CAFFEINE}")

    implementation("com.eternalcode:eternalcode-commons-adventure:${Versions.ETERNALCODE_COMMONS}")
    implementation("com.eternalcode:eternalcode-commons-bukkit:${Versions.ETERNALCODE_COMMONS}")
    implementation("com.eternalcode:eternalcode-commons-shared:${Versions.ETERNALCODE_COMMONS}")
    implementation("com.eternalcode:eternalcode-commons-folia:${Versions.ETERNALCODE_COMMONS}")
    implementation("com.eternalcode:eternalcode-commons-updater:${Versions.ETERNALCODE_COMMONS}")

    compileOnly("me.clip:placeholderapi:${Versions.PLACEHOLDER_API}")

    implementation("com.eternalcode:multification-paper:${Versions.MULTIFICATION}")
    implementation("com.eternalcode:multification-okaeri:${Versions.MULTIFICATION}")
}

paper {
    main = "dev.igoyek.chronicle.ChroniclePlugin"
    authors = listOf("igoyek")
    apiVersion = "1.19"
    prefix = "Chronicle"
    name = "Chronicle"
    generateLibrariesJson = true
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    version = "${project.version}"

    foliaSupported = false

    serverDependencies {
        register("PlaceholderAPI") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}

tasks {
    named("generatePaperPluginDescription") {
        notCompatibleWithConfigurationCache("The plugin-yml paper generator reads Task.project during execution.")
    }

    runPaper.folia.registerTask {
        minecraftVersion("26.2")
    }
}

tasks.shadowJar {
    archiveFileName.set("iLogBlock v${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "net/kyori/**",
        "META-INF/**",
        "kotlin/**",
        "javax/**",
        "org/checkerframework/**",
        "com/google/errorprone/**",
        "com/google/gson/**"
    )

    val prefix = "dev.igoyek.logblock.libs"
    listOf(
        "eu.okaeri",
        "org.bstats",
        "org.yaml",
        "dev.rollczi.litecommands",
        "com.eternalcode.gitcheck",
        "org.json.simple",
        "com.github.benmanes.caffeine",
        "com.eternalcode.commons",
        "com.eternalcode.multification",
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }
}