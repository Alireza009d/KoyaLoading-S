package ir.alireza009.koyaLoading.listener;

import ir.alireza009.koyaLoading.KoyaLoading;
import ir.alireza009.koyaLoading.storage.Storage;
import ir.alireza009.koyaLoading.task.LoadingTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (Storage.getLocation().containsKey(player.getUniqueId())) {
            if (KoyaLoading.getInstance().getConfig().get("Locations." + player.getName()) == null) {
                Location location = Storage.getLocation().get(player.getUniqueId());
                String id = location.getWorld().getName() + "@" + location.getBlockX() + "@" + location.getBlockY() + "@" + location.getBlockZ();
                KoyaLoading.getInstance().getConfig().set("Locations." + player.getName(), id);
                KoyaLoading.getInstance().saveConfig();
                Storage.getLocation().remove(player.getUniqueId());
                return;
            }
        }
    }
}
