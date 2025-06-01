package ir.alireza009.koyaLoading.updateChecker;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class UpdateChecker {

    private final JavaPlugin plugin;
    private final int resourceId;

    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public CompletableFuture<UpdateResult> check() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String latestVersion = reader.readLine();
                reader.close();

                String currentVersion = "v" + plugin.getDescription().getVersion();
                boolean isUpdated = currentVersion.equalsIgnoreCase(latestVersion);

                return new UpdateResult(isUpdated, latestVersion, currentVersion, resourceId);
            } catch (Exception e) {
                plugin.getLogger().warning("⚠️ Failed to check for updates: " + e.getMessage());
                return new UpdateResult(true, "Unknown", "Unknown", resourceId); // assume updated on failure
            }
        });
    }
}

