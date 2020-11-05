package eu.revamp.packages.utils.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class DisarmerUseEvent extends Event implements Cancellable {

    private final HandlerList handlers = new HandlerList();
    private final Player player;
    @Setter private boolean cancelled;

    public DisarmerUseEvent(Player player) {
        this.player = player;
        cancelled = false;
    }
}