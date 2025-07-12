package studio.overmine.overregion;

import studio.overmine.overregion.commands.OverRegenCommand;
import studio.overmine.overregion.controllers.BlockRegenController;
import studio.overmine.overregion.controllers.WorldGuardController;
import studio.overmine.overregion.listeners.BlockRegenListener;
import studio.overmine.overregion.models.License;
import studio.overmine.overregion.utilities.ChatUtil;
import studio.overmine.overregion.utilities.FileConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public class OverRegen extends JavaPlugin {

    private FileConfig licenseFile, configFile;
    private BlockRegenController blockRegenController;
    private WorldGuardController worldGuardController;

    @Override
    public void onEnable() {
        this.licenseFile = new FileConfig(this, "license.yml");

        License license = new License(this, licenseFile.getString("license"));

        if (license.getStatus() == License.LicenseStatus.SUCCESS) {
            this.configFile = new FileConfig(this, "config.yml");

            this.blockRegenController = new BlockRegenController(this);
            this.worldGuardController = new WorldGuardController();

            this.getServer().getPluginManager().registerEvents(new BlockRegenListener(configFile, blockRegenController, worldGuardController), this);
            Objects.requireNonNull(this.getCommand("overregen")).setExecutor(new OverRegenCommand(this));
            Objects.requireNonNull(this.getCommand("overregen")).setTabCompleter(new OverRegenCommand(this));
        }

        ChatUtil.sendConsoleMessage(new String[]{
                ChatUtil.LEGACY_NORMAL_LINE,
                "&6&lOverRegen",
                " &8&l● &fLicense: " + license.getStatus().getColorName(),
                " &8&l● &fVersion: &e" + this.getDescription().getVersion(),
                "",
                "&fIf you have any issues or suggestions",
                "&fjoin our discord server &9https://discord.gg/jreCQVxsfr",
                ChatUtil.LEGACY_NORMAL_LINE
        });
    }

    @Override
    public void onDisable() {
        if (blockRegenController != null) blockRegenController.onDisable();
    }

    public void onReload() {
        this.configFile.reload();
        this.blockRegenController.onReload();
    }
}
