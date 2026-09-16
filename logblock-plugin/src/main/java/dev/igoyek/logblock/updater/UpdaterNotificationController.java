package dev.igoyek.logblock.updater;

import com.eternalcode.commons.concurrent.FutureHandler;
import dev.igoyek.logblock.configuration.PluginConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdaterNotificationController {

    public static final String NEW_VERSION_AVAILABLE = "New version of iLogBlock is available! Check the plugin page for more information.";

    private final UpdaterService updaterService;
    private final PluginConfig pluginConfig;
    private final MiniMessage miniMessage;

    public UpdaterNotificationController(UpdaterService updaterService, PluginConfig pluginConfig, MiniMessage miniMessage) {
        this.updaterService = updaterService;
        this.pluginConfig = pluginConfig;
        this.miniMessage = miniMessage;
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!shouldNotify(player)) {
            return;
        }

        this.updaterService.checkForUpdate()
                .thenAccept(result -> {
                    if (result.isUpdateAvailable()) {
                        player.sendMessage(this.miniMessage.deserialize(NEW_VERSION_AVAILABLE));
                    }
                })
                .exceptionally(FutureHandler::handleException);
    }

    private boolean shouldNotify(Player player) {
        return player.hasPermission("ilogblock.receiveupdates") && this.pluginConfig.settings.notifyAboutUpdates;
    }
}
