package studio.overmine.overregion.tasks;

import studio.overmine.overregion.OverRegen;
import studio.overmine.overregion.controllers.BlockRegenController;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BlockRegenTask extends BukkitRunnable {

    private final OverRegen plugin;
    private final BlockRegenController blockRegenController;

    public BlockRegenTask(OverRegen plugin) {
        this.plugin = plugin;
        this.blockRegenController = plugin.getBlockRegenController();
    }

    @Override
    public void run() {
        Map<BlockState, Long> blockStateMap = blockRegenController.getBlockStateMap();
        Map<List<BlockState>, Long> blockListMap = blockRegenController.getBlockListMap();

        if (blockStateMap.isEmpty() && blockListMap.isEmpty()) {
            blockRegenController.stopBlockRegenTask();
            return;
        }

        Iterator<BlockState> blockStateIterator = blockStateMap.keySet().iterator();

        while (blockStateIterator.hasNext()) {
            BlockState blockState = blockStateIterator.next();
            long time = blockRegenController.getBlockStateRemainingTime(blockState);

            if (time <= 0) {
                blockRegenController.restoreBlockState(blockState);
                blockStateIterator.remove();
            }
        }

        Iterator<List<BlockState>> blockListIterator = blockListMap.keySet().iterator();

        while (blockListIterator.hasNext()) {
            List<BlockState> blockList = blockListIterator.next();
            long time = blockRegenController.getBlockListRemainingTime(blockList);

            if (time <= 0) {
                blockRegenController.restoreBlockList(blockList);
                blockListIterator.remove();
            }
        }
    }

    public void start() {
        this.runTaskTimer(plugin, 20L, 20L);
    }
}
