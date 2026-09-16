package dev.igoyek.logblock.event;

import org.bukkit.event.Listener;

public interface DynamicListener<E> extends Listener {

    void onEvent(E event);
}
