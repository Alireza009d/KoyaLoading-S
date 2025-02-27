package ir.alireza009.koyaLoading;

import ir.alireza009.koyaLoading.listener.PlayerJoinListener;
import ir.alireza009.koyaLoading.listener.PlayerMoveListener;
import ir.alireza009.koyaLoading.listener.PlayerQuitListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class KoyaLoading extends JavaPlugin {
    private static KoyaLoading plugin;
    public static KoyaLoading getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        registerListeners();
        saveDefaultConfig();
        int pluginId = 24687;
        Metrics metrics = new Metrics(this, pluginId);
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
}
