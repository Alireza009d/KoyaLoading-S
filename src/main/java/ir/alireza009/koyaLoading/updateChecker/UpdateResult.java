package ir.alireza009.koyaLoading.updateChecker;

public class UpdateResult {
    private final boolean isUpdated;
    private final String latestVersion;
    private final String currentVersion;
    private final int resourceId;

    public UpdateResult(boolean isUpdated, String latestVersion, String currentVersion, int resourceId) {
        this.isUpdated = isUpdated;
        this.latestVersion = latestVersion;
        this.currentVersion = currentVersion;
        this.resourceId = resourceId;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public int getResourceId() {
        return resourceId;
    }
}

