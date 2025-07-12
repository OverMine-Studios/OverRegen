package studio.overmine.overregion.controllers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import java.util.List;

public class WorldGuardController {

    public boolean isNotInRegion(org.bukkit.Location location, List<String> regionsFromConfig) {
        Location loc = BukkitAdapter.adapt(location);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        ApplicableRegionSet regions = query.getApplicableRegions(loc);
        ProtectedRegion highestPriorityRegion = null;

        for (ProtectedRegion region : regions.getRegions()) {
            if (highestPriorityRegion == null || region.getPriority() > highestPriorityRegion.getPriority()) {
                highestPriorityRegion = region;
            }
        }

        return highestPriorityRegion == null || !regionsFromConfig.contains(highestPriorityRegion.getId());
    }
}
