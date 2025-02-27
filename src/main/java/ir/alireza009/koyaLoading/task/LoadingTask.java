package ir.alireza009.koyaLoading.task;

import ir.alireza009.koyaLoading.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import ir.alireza009.koyaLoading.KoyaLoading;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LoadingTask {

    private static final FileConfiguration config = KoyaLoading.getInstance().getConfig();

    private static void playTeleportSound(Player player) {
        if (!config.getBoolean("Sound.Enable")) return;

        String soundName = config.getString("Sound.Sound", "ENTITY_ENDERMAN_TELEPORT");
        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            player.playSound(player.getLocation(), sound, 1, 1);
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid sound in config: " + soundName);
        }
    }

    public static void loadingTask(Player player, Location location, int delay) {
        player.setGameMode(GameMode.SPECTATOR);
        Location loadingLocation = new Location(location.getWorld(), location.getX(), location.getY()+45, location.getZ(), 0.0F, 90.0F);
        player.teleport(loadingLocation);

        int[] teleportTimes = {40, 70, 100, 130, 160, 190, 220, 250, 280};

        new BukkitRunnable() {
            int time = 0;
            int index = 0;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                time++;

                if (index < teleportTimes.length && time == teleportTimes[index]) {
                    if (time == 280) {
                        player.setGameMode(GameMode.SURVIVAL);
                        Storage.getLocation().remove(player.getUniqueId());
                        config.set("Locations." + player.getName(), null);
                        KoyaLoading.getInstance().saveConfig();
                        loadingLocation.add(0, -4.5, 0);
                        loadingLocation.setPitch(0.0F);
                        loadingLocation.setYaw(90.0F);
                    } else {
                        loadingLocation.add(0, -5, 0);
                    }

                    player.teleport(loadingLocation);
                    playTeleportSound(player);
                    index++;
                }

                if (time >= 280) {
                    cancel();
                }
            }
        }.runTaskTimer(KoyaLoading.getInstance(), delay, 1);
    }
}
