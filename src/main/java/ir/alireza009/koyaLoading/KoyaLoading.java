package ir.alireza009.koyaLoading;

import ir.alireza009.koyaLoading.listener.PlayerJoinListener;
import ir.alireza009.koyaLoading.listener.PlayerMoveListener;
import ir.alireza009.koyaLoading.listener.PlayerQuitListener;
import ir.alireza009.koyaLoading.storage.FileManager;
import ir.alireza009.koyaLoading.updateChecker.UpdateChecker;
import ir.alireza009.koyaLoading.updateChecker.UpdateResult;
import ir.alireza009.koyaLoading.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class KoyaLoading extends JavaPlugin {
    private static KoyaLoading plugin;
    public static KoyaLoading getInstance() {
        return plugin;
    }

    private static UpdateResult updateResult;
    public static UpdateResult getUpdateResult() { return updateResult; }

    @Override
    public void onEnable() {
        plugin = this;
        registerListeners();
        registerFileManager();
        saveDefaultConfig();
        if (getConfig().getBoolean("Check-For-Update", true)) {
            checkUpdate();
        } else {
            updateResult = new UpdateResult(true, "Unknown", "Unknown", 122445);
        }
        int pluginId = 24687;
        Metrics metrics = new Metrics(this, pluginId);
    }

    public void checkUpdate() {
        new UpdateChecker(this, 122445).check().thenAccept(result -> {
            if (!result.isUpdated()) {
                Bukkit.getConsoleSender().sendMessage(Utils.colorize("&8--------------------------------------------------"));
                Bukkit.getConsoleSender().sendMessage(Utils.colorize("&6  &eA new version of &9KoyaLoading &eis available!"));
                Bukkit.getConsoleSender().sendMessage(Utils.colorize("&7┃ &fCurrent version: &c" + result.getCurrentVersion()));
                Bukkit.getConsoleSender().sendMessage(Utils.colorize("&7┃ &fLatest version: &a" + result.getLatestVersion()));
                Bukkit.getConsoleSender().sendMessage(Utils.colorize("&7┃ &fDownload: &bhttps://spigotmc.org/resources/" + result.getResourceId()));
                Bukkit.getConsoleSender().sendMessage(Utils.colorize("&8--------------------------------------------------"));

            } else {
                getLogger().info("KoyaLoading is up to date.");
            }
            updateResult = result;
        });
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
    }

    private void registerFileManager() {
        FileManager config = new FileManager(getDataFolder(), "config.yml");
        config.saveDefaultConfig();
    }
}
