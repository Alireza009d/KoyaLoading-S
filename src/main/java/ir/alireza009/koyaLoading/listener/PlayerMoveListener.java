package ir.alireza009.koyaLoading.listener;

import ir.alireza009.koyaLoading.storage.Storage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (Storage.getLocation().containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
