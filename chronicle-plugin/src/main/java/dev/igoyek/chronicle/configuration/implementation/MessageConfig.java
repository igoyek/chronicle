package dev.igoyek.chronicle.configuration.implementation;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

public class MessageConfig extends OkaeriConfig {

    public Notice missingPermission = Notice.chat("<red>You don't have permission to do that! ({PERMISSION})");

    public Notice playerNotFound = Notice.chat("<red>Player not found!");

    public Notice playerOnly = Notice.chat("<red>This command can only be used by players!");

    public Notice invalidCommandUsage = Notice.chat("<red>Invalid command usage! Correct usage: {USAGE}");
}
