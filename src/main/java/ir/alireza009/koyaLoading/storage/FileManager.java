package ir.alireza009.koyaLoading.storage;

import ir.alireza009.koyaLoading.KoyaLoading;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class FileManager {
    private final File folder;
    private final String fileName;
    private File configFile = null;
    private FileConfiguration config = null;

    public FileManager(File folder, String fileName) {
        this.folder = folder;
        this.fileName = fileName;
        saveDefaultConfig();  // Ensure the default config is saved upon initialization
    }

    /**
     * Reloads the configuration from the file and applies the default values.
     */
    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(folder, fileName);
        }

        // Load the configuration from the file
        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        // Load default configuration from the plugin resources if available
        InputStream defaultStream = KoyaLoading.getInstance().getResource(fileName);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.config.setDefaults(defaultConfig);  // Apply default values
        }
    }

    /**
     * Gets the current configuration, reloading if necessary.
     *
     * @return the current FileConfiguration
     */
    public FileConfiguration getConfig() {
        if (this.config == null) reloadConfig();  // Reload the config if it's null
        return this.config;
    }

    /**
     * Saves the current configuration to the file.
     */
    public void save() {
        if (this.config == null || this.configFile == null) {
            KoyaLoading.getInstance().getLogger().warning("Config or configFile is null, unable to save.");
            return;
        }

        try {
            this.config.save(this.configFile);  // Save the config to the file
        } catch (IOException e) {
            KoyaLoading.getInstance().getLogger().log(Level.SEVERE, "Couldn't save config to " + this.configFile, e);
        }
    }

    /**
     * Saves the default configuration to the file if it does not already exist.
     */
    public void saveDefaultConfig() {
        if (this.configFile == null) {
            this.configFile = new File(folder, fileName);
        }

        InputStream defaultStream = KoyaLoading.getInstance().getResource(fileName);

        if (this.configFile.exists()) {
            if (defaultStream != null) {
                FileConfiguration currentConfig = YamlConfiguration.loadConfiguration(this.configFile);
                FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));

                String currentVersion = currentConfig.getString("ConfigVersion", "unknown");
                String defaultVersion = defaultConfig.getString("ConfigVersion", "unknown");

                if (!currentVersion.equals(defaultVersion)) {
                    File backupFile = new File(folder, fileName.replace(".yml", "") + "-old-" + currentVersion + ".yml");
                    try {
                        currentConfig.save(backupFile);
                        KoyaLoading.getInstance().getLogger().info("Backed up old " + fileName + " to " + backupFile.getName());
                        KoyaLoading.getInstance().saveResource(fileName, true);
                    } catch (IOException e) {
                        KoyaLoading.getInstance().getLogger().log(Level.SEVERE, "Failed to backup " + fileName, e);
                    }
                }
            }
            return;
        }

        // If file doesn't exist
        if (defaultStream != null) {
            KoyaLoading.getInstance().saveResource(fileName, false);
        } else {
            try {
                this.configFile.createNewFile();
            } catch (IOException e) {
                KoyaLoading.getInstance().getLogger().log(Level.SEVERE, "Failed to create " + fileName, e);
            }
        }
    }

}
