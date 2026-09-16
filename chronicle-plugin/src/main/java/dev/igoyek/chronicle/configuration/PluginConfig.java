package dev.igoyek.chronicle.configuration;

import dev.igoyek.chronicle.configuration.implementation.MessageConfig;
import dev.igoyek.chronicle.database.DatabaseConfig;
import eu.okaeri.configs.OkaeriConfig;

public class PluginConfig extends OkaeriConfig {

    public Settings settings = new Settings();

    public DatabaseConfig database = new DatabaseConfig();

    public MessageConfig messages = new MessageConfig();

    public static class Settings extends OkaeriConfig {
        public boolean notifyAboutUpdates = true;
    }
}
