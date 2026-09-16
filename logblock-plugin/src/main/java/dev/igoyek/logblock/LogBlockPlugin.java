package dev.igoyek.logblock;

import com.eternalcode.commons.adventure.AdventureLegacyColorPostProcessor;
import com.eternalcode.commons.adventure.AdventureLegacyColorPreProcessor;
import com.eternalcode.multification.notice.Notice;
import com.google.common.base.Stopwatch;
import dev.igoyek.logblock.configuration.ConfigurationService;
import dev.igoyek.logblock.configuration.PluginConfig;
import dev.igoyek.logblock.handler.InvalidUsageHandlerImpl;
import dev.igoyek.logblock.handler.MissingPermissionHandlerImpl;
import dev.igoyek.logblock.notification.NoticeService;
import dev.igoyek.logblock.updater.UpdaterService;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class LogBlockPlugin extends JavaPlugin implements LogBlockApi {

    private static final String FALLBACK_PREFIX = "ilogblock";
    private static final int BSTATS_METRICS_ID = 0;

    private LiteCommands<CommandSender> liteCommands;
    private boolean apiInitialized;

    @Override
    public void onEnable() {
        Stopwatch started = Stopwatch.createStarted();
        Server server = this.getServer();

        File dataFolder = this.getDataFolder();

        ConfigurationService configurationService = new ConfigurationService();

        PluginConfig pluginConfig = configurationService.create(PluginConfig.class, new File(dataFolder, "configuration.yml"));

        UpdaterService updaterService = new UpdaterService(this.getDescription());

        MiniMessage miniMessage = MiniMessage.builder()
                .postProcessor(new AdventureLegacyColorPostProcessor())
                .preProcessor(new AdventureLegacyColorPreProcessor())
                .build();

        NoticeService noticeService = new NoticeService(pluginConfig, miniMessage);

        this.liteCommands = LiteBukkitFactory.builder(FALLBACK_PREFIX, this, server)
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

        new Metrics(this, BSTATS_METRICS_ID);

        long millis = started.elapsed().toMillis();
        this.getLogger().info("iLogBlock plugin enabled in " + millis + "ms!");
    }

    @Override
    public void onDisable() {
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }

        if (this.apiInitialized) {
            LogBlockProvider.deinitialize();
            this.apiInitialized = false;
        }
    }
}
