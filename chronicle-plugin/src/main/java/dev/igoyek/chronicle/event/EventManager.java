package dev.igoyek.chronicle.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class EventManager {

    private final Plugin plugin;

    public EventManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public <T extends Event> T publishEvent(T event) {
        this.plugin.getServer().getPluginManager().callEvent(event);

        return event;
    }

    public void subscribe(Listener... listeners) {
        for (Listener listener : listeners) {
            this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
        }
    }

    public <E extends Event, L extends DynamicListener<E>> void subscribe(Class<E> type, EventPriority priority, L listener) {
        this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
        this.plugin.getServer().getPluginManager().registerEvent(type, listener, priority, (l, event) -> {
            if (type.isInstance(event)) {
                listener.onEvent(type.cast(event));
            }
        }, plugin);
    }
}
