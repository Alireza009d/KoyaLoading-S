package ir.alireza009.koyaLoading.listener;

import ir.alireza009.koyaLoading.KoyaLoading;
import ir.alireza009.koyaLoading.storage.Storage;
import ir.alireza009.koyaLoading.task.LoadingTask;
import ir.alireza009.koyaLoading.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        if (!KoyaLoading.getInstance().getConfig().getBoolean("Resource-pack.Enable")) return;
        Player player = event.getPlayer();
        Utils.checkUpdate(player);
        String permission = KoyaLoading.getInstance().getConfig().getString("Bypass-Permission", "");
        if (!permission.isEmpty() && player.hasPermission(permission)) {
            player.sendMessage(Utils.colorize("&8[&9KoyaLoading&8] &7You bypassed loading&7."));

            return;
        }
        PlayerResourcePackStatusEvent.Status status = event.getStatus();

        if (status.equals(PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) ||
        status.equals(PlayerResourcePackStatusEvent.Status.DECLINED) ||
        status.equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)) {
            int delay = KoyaLoading.getInstance().getConfig().getInt("Delay");
            if (KoyaLoading.getInstance().getConfig().get("Locations." + player.getName()) == null) {
                Storage.getLocation().put(player.getUniqueId(), player.getLocation());
                LoadingTask.loadingTask(player, player.getLocation(), delay);
            } else {
                String id = KoyaLoading.getInstance().getConfig().getString("Locations." + player.getName());
                String[] xyz = id.split("@");
                Location location = new Location(Bukkit.getWorld(xyz[0]), Double.valueOf(xyz[1]), Double.valueOf(xyz[2]), Double.valueOf(xyz[3]));

                Storage.getLocation().put(player.getUniqueId(), location);
                LoadingTask.loadingTask(player, location, delay);
                return;
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (KoyaLoading.getInstance().getConfig().getBoolean("Resource-pack.Enable")) return;
        Player player = event.getPlayer();
        Utils.checkUpdate(player);
        String permission = KoyaLoading.getInstance().getConfig().getString("Bypass-Permission", "");
        if (!permission.isEmpty() && player.hasPermission(permission)) {
            player.sendMessage(Utils.colorize("&8[&9KoyaLoading&8] &7You bypassed loading&7."));

            return;
        }
        int delay = KoyaLoading.getInstance().getConfig().getInt("Delay");
        if (KoyaLoading.getInstance().getConfig().get("Locations." + player.getName()) == null) {
            Storage.getLocation().put(player.getUniqueId(), player.getLocation());
            LoadingTask.loadingTask(player, player.getLocation(), delay);
        } else {
            String id = KoyaLoading.getInstance().getConfig().getString("Locations." + player.getName());
            String[] xyz = id.split("@");
            Location location = new Location(Bukkit.getWorld(xyz[0]), Double.valueOf(xyz[1]), Double.valueOf(xyz[2]), Double.valueOf(xyz[3]));

            Storage.getLocation().put(player.getUniqueId(), location);
            LoadingTask.loadingTask(player, location, delay);
            return;
        }

    }
}
