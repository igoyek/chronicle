package dev.igoyek.chronicle.configuration;

import dev.igoyek.chronicle.configuration.settings.MessageSettings;
import eu.okaeri.configs.OkaeriConfig;

public class PluginConfig extends OkaeriConfig {

    public Settings settings = new Settings();

    public MessageSettings messages = new MessageSettings();

    public static class Settings extends OkaeriConfig {
        public boolean notifyAboutUpdates = true;
    }
}
