package studio.overmine.overregion.controllers;

import studio.overmine.overregion.OverRegen;
import studio.overmine.overregion.tasks.BlockRegenTask;
import studio.overmine.overregion.utilities.FileConfig;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.*;

public class BlockRegenController {

    private final OverRegen plugin;
    private final FileConfig configFile;

    @Getter private final Map<BlockState, Long> blockStateMap;
    @Getter private final Map<List<BlockState>, Long> blockListMap;
    private Set<String> blacklistBlock;

    private BlockRegenTask blockRegenTask;

    public BlockRegenController(OverRegen plugin) {
        this.plugin = plugin;
        this.configFile = plugin.getConfigFile();
        this.blockStateMap = new HashMap<>();
        this.blockListMap = new HashMap<>();
        this.onReload();
    }

    public boolean isRegenBlock(Location location) {
        return blockStateMap.keySet().stream().anyMatch(blockState -> blockState.getLocation().equals(location))
                || blockListMap.keySet().stream().anyMatch(blockList -> blockList.stream().anyMatch(blockState -> blockState.getLocation().equals(location)));
    }

    public boolean isBlacklistBlock(BlockState blockState) {
        String blockStateMaterial = blockState.getBlock().getType().name();

        for (String material : blacklistBlock) {
            if (blockStateMaterial.equals(material)) {
                return true;
            }
        }

        return false;
    }

    public List<BlockState> getBlockStateFormatted(List<Block> blockList) {
        List<BlockState> blockStateList = new ArrayList<>();

        for (Block block : blockList) {
            String blockName = block.getType().name();
            if (blacklistBlock.contains(blockName)) continue;

            blockStateList.add(block.getState());
        }

        return blockStateList;
    }

    public void startBlockRegenTask() {
        if (blockRegenTask == null) {
            blockRegenTask = new BlockRegenTask(plugin);
            blockRegenTask.start();
        }
    }

    public void stopBlockRegenTask() {
        if (blockRegenTask != null) {
            blockRegenTask.cancel();
            blockRegenTask = null;
        }
    }

    public void addBlockState(BlockState blockState, long delay) {
        blockStateMap.put(blockState, System.currentTimeMillis() + delay);
        startBlockRegenTask();
    }

    public void addBlockList(List<BlockState> blockList, long delay) {
        blockListMap.put(blockList, System.currentTimeMillis() + delay);
        startBlockRegenTask();
    }

    public long getBlockStateRemainingTime(BlockState blockState) {
        return blockStateMap.get(blockState) - System.currentTimeMillis();
    }

    public long getBlockListRemainingTime(List<BlockState> blockList) {
        return blockListMap.get(blockList) - System.currentTimeMillis();
    }

    public void restoreBlockState(BlockState blockState) {
        blockState.update(true);
    }

    public void restoreBlockList(List<BlockState> blockList) {
        blockList.forEach(blockState -> blockState.update(true));
    }

    public void onDisable() {
        this.blockStateMap.keySet().forEach(this::restoreBlockState);
        this.blockListMap.keySet().forEach(this::restoreBlockList);
    }

    public void onReload() {
        this.blacklistBlock = new HashSet<>(configFile.getStringList("blacklist-block"));
    }
}
