package dev.igoyek.chronicle.handler;

import dev.igoyek.chronicle.configuration.PluginConfig;
import dev.igoyek.chronicle.notification.NoticeService;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import org.bukkit.command.CommandSender;

public class MissingPermissionHandlerImpl implements MissingPermissionsHandler<CommandSender> {

    private final PluginConfig pluginConfig;
    private final NoticeService noticeService;

    public MissingPermissionHandlerImpl(PluginConfig pluginConfig, NoticeService noticeService) {
        this.pluginConfig = pluginConfig;
        this.noticeService = noticeService;
    }

    @Override
    public void handle(Invocation<CommandSender> invocation, MissingPermissions missingPermissions, ResultHandlerChain<CommandSender> chain) {
        String joinedText = missingPermissions.asJoinedText();

        this.noticeService.create()
                .viewer(invocation.sender())
                .notice(this.pluginConfig.messages.missingPermission)
                .placeholder("{PERMISSIONS}", joinedText)
                .send();
    }
}
