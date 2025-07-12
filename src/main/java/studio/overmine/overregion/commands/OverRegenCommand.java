package studio.overmine.overregion.commands;

import org.bukkit.command.TabCompleter;
import studio.overmine.overregion.OverRegen;
import studio.overmine.overregion.utilities.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class OverRegenCommand implements CommandExecutor, TabCompleter {

    private final OverRegen plugin;

    public OverRegenCommand(OverRegen plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            ChatUtil.sendMessage(sender, "&cUsage: /" + label + " reload");
            return false;
        }

        plugin.onReload();
        ChatUtil.sendMessage(sender, "&aOverRegen has been reloaded.");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? List.of("reload") : null;
    }
}
