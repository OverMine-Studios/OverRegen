package studio.overmine.overregion.listeners;

import studio.overmine.overregion.controllers.BlockRegenController;
import studio.overmine.overregion.controllers.WorldGuardController;
import studio.overmine.overregion.utilities.FileConfig;
import studio.overmine.overregion.utilities.TimeUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

public class BlockRegenListener implements Listener {

    private final FileConfig configFile;
    private final BlockRegenController blockRegenController;
    private final WorldGuardController worldGuardController;

    public BlockRegenListener(FileConfig configFile, BlockRegenController blockRegenController, WorldGuardController worldGuardController) {
        this.configFile = configFile;
        this.blockRegenController = blockRegenController;
        this.worldGuardController = worldGuardController;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

        BlockState blockState = event.getBlock().getState();
        Location location = blockState.getLocation();

        if (blockRegenController.isBlacklistBlock(blockState) || worldGuardController.isNotInRegion(location, configFile.getStringList("whitelist-region"))) return;

        boolean isRegenBlock = blockRegenController.isRegenBlock(location);

        event.setCancelled(false);
        event.setDropItems(isRegenBlock);

        if (isRegenBlock) return;

        blockRegenController.addBlockState(blockState, TimeUtil.formatLong(configFile.getString("block-regen-time.break")));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

        BlockState blockState = event.getBlockReplacedState();
        Location location = blockState.getLocation();

        if (blockRegenController.isBlacklistBlock(blockState) || worldGuardController.isNotInRegion(location, configFile.getStringList("whitelist-region"))) return;

        event.setCancelled(false);

        if (blockRegenController.isRegenBlock(location)) return;

        blockRegenController.addBlockState(blockState, TimeUtil.formatLong(configFile.getString("block-regen-time.place")));
    }

    @EventHandler
    public void onRespawnAnchorExplode(BlockExplodeEvent event) {
        Location location = event.getBlock().getLocation();
        if (worldGuardController.isNotInRegion(location, configFile.getStringList("whitelist-region"))) return;

        event.setYield(0);

        List<BlockState> blockList = blockRegenController.getBlockStateFormatted(event.blockList());
        blockRegenController.addBlockList(blockList, TimeUtil.formatLong(configFile.getString("block-regen-time.explode")));
    }

    @EventHandler
    public void onEndCrystalExplode(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof EnderCrystal)) return;

        Location location = event.getLocation();
        if (worldGuardController.isNotInRegion(location, configFile.getStringList("whitelist-region"))) return;

        event.setYield(0);

        List<BlockState> blockList = blockRegenController.getBlockStateFormatted(event.blockList());
        blockRegenController.addBlockList(blockList, TimeUtil.formatLong(configFile.getString("block-regen-time.explode")));
    }
}
