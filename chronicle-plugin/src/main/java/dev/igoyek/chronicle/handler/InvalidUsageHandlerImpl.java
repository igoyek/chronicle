package dev.igoyek.chronicle.handler;

import dev.igoyek.chronicle.configuration.PluginConfig;
import dev.igoyek.chronicle.notification.NoticeService;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;

public class InvalidUsageHandlerImpl implements InvalidUsageHandler<CommandSender> {

    private final PluginConfig pluginConfig;
    private final NoticeService noticeService;

    public InvalidUsageHandlerImpl(PluginConfig pluginConfig, NoticeService noticeService) {
        this.pluginConfig = pluginConfig;
        this.noticeService = noticeService;
    }

    @Override
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        Schematic schematic = result.getSchematic();

        for (String usage : schematic.all()) {
            this.noticeService.create()
                    .viewer(invocation.sender())
                    .notice(this.pluginConfig.messages.invalidCommandUsage)
                    .placeholder("{USAGE}", usage)
                    .send();
        }
    }
}
