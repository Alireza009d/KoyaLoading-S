package ir.alireza009.koyaLoading.storage;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Storage {
    private static final HashMap<UUID, Location> getLocation = new HashMap<>();
    public static HashMap<UUID, Location> getLocation() { return getLocation; }

}
