package ir.alireza009.koyaLoading.utils;

import ir.alireza009.koyaLoading.KoyaLoading;
import ir.alireza009.koyaLoading.updateChecker.UpdateResult;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void checkUpdate(Player player) {
        UpdateResult updateResult = KoyaLoading.getUpdateResult();

        if (player.hasPermission("KoyaLoading.admin") && !updateResult.isUpdated()) {
            Bukkit.getScheduler().runTaskLater(KoyaLoading.getInstance(), () -> {
                player.sendMessage(Utils.colorize("&8[&9KoyaLoading&8] &9» &eA new version of &9KoyaLoading &eis available!"));
                player.sendMessage(Utils.colorize("&8┃ &fCurrent version: &c" + updateResult.getCurrentVersion()));
                player.sendMessage(Utils.colorize("&8┃ &fLatest version: &a" + updateResult.getLatestVersion()));
                player.sendMessage(Utils.colorize("&8┃ &fDownload: &bhttps://spigotmc.org/resources/" + updateResult.getResourceId()));
            }, 20L); // 1 second delay

        }
    }
}
