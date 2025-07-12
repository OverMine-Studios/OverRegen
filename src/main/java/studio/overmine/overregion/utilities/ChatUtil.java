package studio.overmine.overregion.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@UtilityClass
public class ChatUtil {

    public String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(translate(message));
    }
}
