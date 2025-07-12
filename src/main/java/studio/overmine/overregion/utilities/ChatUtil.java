package studio.overmine.overregion.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@UtilityClass
public class ChatUtil {

    public String LEGACY_NORMAL_LINE = "&7&m-----------------------------------------";

    public String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String[] translate(String[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = translate(array[i]);
        }
        return array;
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(translate(message));
    }

    public void sendConsoleMessage(String[] message) {
        Bukkit.getConsoleSender().sendMessage(translate(message));
    }
}
