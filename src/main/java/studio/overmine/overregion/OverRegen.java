package studio.overmine.overregion;

import studio.overmine.overregion.commands.OverRegenCommand;
import studio.overmine.overregion.controllers.BlockRegenController;
import studio.overmine.overregion.controllers.WorldGuardController;
import studio.overmine.overregion.listeners.BlockRegenListener;
import studio.overmine.overregion.utilities.FileConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public class OverRegen extends JavaPlugin {

    private FileConfig configFile;
    private BlockRegenController blockRegenController;
    private WorldGuardController worldGuardController;

    @Override
    public void onEnable() {
        this.configFile = new FileConfig(this, "config.yml");
        this.blockRegenController = new BlockRegenController(this);
        this.worldGuardController = new WorldGuardController();

        this.getServer().getPluginManager().registerEvents(new BlockRegenListener(configFile, blockRegenController, worldGuardController), this);
        Objects.requireNonNull(this.getCommand("overregen")).setExecutor(new OverRegenCommand(this));
    }

    @Override
    public void onDisable() {
        this.blockRegenController.onDisable();
    }

    public void onReload() {
        this.configFile.reload();
        this.blockRegenController.onReload();
    }
}
