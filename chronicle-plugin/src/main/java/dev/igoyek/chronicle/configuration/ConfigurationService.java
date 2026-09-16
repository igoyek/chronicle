package dev.igoyek.chronicle.configuration;

import com.eternalcode.multification.notice.resolver.NoticeResolverDefaults;
import com.eternalcode.multification.notice.resolver.NoticeResolverRegistry;
import com.eternalcode.multification.notice.resolver.sound.SoundAdventureResolver;
import com.eternalcode.multification.okaeri.MultificationSerdesPack;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConfigurationService {

    private final Set<OkaeriConfig> configs = new HashSet<>();

    public <T extends OkaeriConfig> T create(Class<T> config, File file) {
        T configInstance = ConfigManager.create(config);

        YamlSnakeYamlConfigurer configurer = new YamlSnakeYamlConfigurer();
        NoticeResolverRegistry noticeRegistry = NoticeResolverDefaults.createRegistry()
                .registerResolver(new SoundAdventureResolver());

        configInstance.withConfigurer(configurer,
                new SerdesCommons(),
                new SerdesBukkit(),
                new MultificationSerdesPack(noticeRegistry)
        );

        configInstance.withBindFile(file);
        configInstance.withRemoveOrphans(true);
        configInstance.saveDefaults();
        configInstance.load(true);

        this.configs.add(configInstance);

        return configInstance;
    }

    public void reload() {
        for (OkaeriConfig config : this.configs) {
            config.load();
        }
    }
}
