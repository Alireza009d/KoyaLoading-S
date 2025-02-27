package ir.alireza009.koyaLoading.task;

import ir.alireza009.koyaLoading.storage.Storage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import ir.alireza009.koyaLoading.KoyaLoading;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999, 1, false, false));
        final Location[] loadingLocation = {new Location(location.getWorld(), location.getX(), location.getY() + 45, location.getZ(), 0.0F, 90.0F)};
        int y = countSolidBlocks(location, loadingLocation[0]);
        loadingLocation[0].add(0, y, 0);
        player.teleport(loadingLocation[0]);
        new BukkitRunnable() {
            int time = 0;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                if (time == 1) player.setGameMode(GameMode.SPECTATOR);
                time++;

                if ((loadingLocation[0].getBlockY() - location.getBlockY()) < 10) {
                    player.teleport(location);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    Storage.getLocation().remove(player.getUniqueId());
                    config.set("Locations." + player.getName(), null);
                    KoyaLoading.getInstance().saveConfig();
                    cancel();
                    return;
                }

                if ((time % 30) == 0) {
                    loadingLocation[0].add(0, -5, 0);
                    if (loadingLocation[0].getBlock().getType().isSolid()) loadingLocation[0] = getSafeLocation(loadingLocation[0]);
                    player.teleport(loadingLocation[0]);
                    playTeleportSound(player);

                }
            }
        }.runTaskTimer(KoyaLoading.getInstance(), delay, 1);
    }

    private static Location getSafeLocation(Location loc) {
        while (loc.getBlock().getType().isSolid()) {
            loc.add(0, -1, 0);
        }
        return loc;
    }

    private static int countSolidBlocks(Location loc1, Location loc2) {
        World world = loc1.getWorld();
        int count = 0;

        if (loc1.getBlockX() != loc2.getBlockX()) {
            int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
            int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());

            for (int x = minX; x <= maxX; x++) {
                Block block = world.getBlockAt(x, loc1.getBlockY(), loc1.getBlockZ());
                if (!block.isEmpty()) {
                    count++;
                }
            }
        } else if (loc1.getBlockY() != loc2.getBlockY()) {
            int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
            int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());

            for (int y = minY; y <= maxY; y++) {
                Block block = world.getBlockAt(loc1.getBlockX(), y, loc1.getBlockZ());
                if (!block.isEmpty()) {
                    count++;
                }
            }
        } else if (loc1.getBlockZ() != loc2.getBlockZ()) {
            int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
            int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

            for (int z = minZ; z <= maxZ; z++) {
                Block block = world.getBlockAt(loc1.getBlockX(), loc1.getBlockY(), z);
                if (!block.isEmpty()) {
                    count++;
                }
            }
        }

        return count;
    }

}
