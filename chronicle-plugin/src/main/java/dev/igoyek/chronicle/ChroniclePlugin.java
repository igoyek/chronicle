package dev.igoyek.chronicle;

import com.eternalcode.commons.adventure.AdventureLegacyColorPostProcessor;
import com.eternalcode.commons.adventure.AdventureLegacyColorPreProcessor;
import com.eternalcode.multification.notice.Notice;
import com.google.common.base.Stopwatch;
import dev.igoyek.chronicle.configuration.ConfigurationService;
import dev.igoyek.chronicle.configuration.PluginConfig;
import dev.igoyek.chronicle.database.DatabaseManager;
import dev.igoyek.chronicle.handler.InvalidUsageHandlerImpl;
import dev.igoyek.chronicle.handler.MissingPermissionHandlerImpl;
import dev.igoyek.chronicle.notification.NoticeService;
import dev.igoyek.chronicle.updater.UpdaterService;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ChroniclePlugin extends JavaPlugin implements ChronicleApi {

    private static final String COMMAND_FALLBACK_PREFIX = "chronicle";
    private static final int BSTATS_METRICS_ID = 0;

    private LiteCommands<CommandSender> liteCommands;
    private DatabaseManager databaseManager;
    private boolean apiInitialized;

    @Override
    public void onEnable() {
        Stopwatch startupTimer = Stopwatch.createStarted();
        Server server = this.getServer();
        File dataFolder = this.getDataFolder();

        ConfigurationService configurationService = new ConfigurationService();
        PluginConfig pluginConfig = configurationService.create(PluginConfig.class, new File(dataFolder, "configuration.yml"));

        this.databaseManager = new DatabaseManager(this.getLogger(), dataFolder, pluginConfig.database);
        try {
            this.databaseManager.connect();
        } catch (Exception exception) {
            this.getLogger().severe("Could not initialize the database, disabling plugin: " + exception.getMessage());
            server.getPluginManager().disablePlugin(this);
            return;
        }

        UpdaterService updaterService = new UpdaterService(this.getDescription());

        MiniMessage miniMessage = MiniMessage.builder()
                .postProcessor(new AdventureLegacyColorPostProcessor())
                .preProcessor(new AdventureLegacyColorPreProcessor())
                .build();

        NoticeService noticeService = new NoticeService(pluginConfig, miniMessage);

        this.liteCommands = LiteBukkitFactory.builder(COMMAND_FALLBACK_PREFIX, this, server)
                .message(LiteBukkitMessages.PLAYER_NOT_FOUND, pluginConfig.messages.playerNotFound)
                .message(LiteBukkitMessages.PLAYER_ONLY, pluginConfig.messages.playerOnly)

                .invalidUsage(new InvalidUsageHandlerImpl(pluginConfig, noticeService))
                .missingPermission(new MissingPermissionHandlerImpl(pluginConfig, noticeService))

                .commands(

                )

                .result(Notice.class, (invocation, result, chain) -> noticeService.create()
                        .viewer(invocation.sender())
                        .notice(result)
                        .send())

                .build();

        ChronicleProvider.initialize(this);
        this.apiInitialized = true;

        new Metrics(this, BSTATS_METRICS_ID);

        this.getLogger().info("Chronicle enabled in " + startupTimer.elapsed().toMillis() + "ms.");
    }

    @Override
    public void onDisable() {
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }

        if (this.apiInitialized) {
            ChronicleProvider.deinitialize();
            this.apiInitialized = false;
        }

        if (this.databaseManager != null) {
            this.databaseManager.close();
        }
    }
}
