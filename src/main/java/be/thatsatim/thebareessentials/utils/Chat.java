package be.thatsatim.thebareessentials.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Chat {
    public static String color (String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void message(Object receiver, String message, FileConfiguration config, String replace) {
        if (receiver instanceof Player) {
            if (replace == null) {
                ((Player) receiver).sendMessage(Chat.color(config.getString(message)));
                return;
            }
            ((Player) receiver).sendMessage(Chat.color(config.getString(message).replace("<PLAYER>", replace)));
        }
        if (receiver instanceof ConsoleCommandSender) {
            if (replace == null) {
                ((ConsoleCommandSender) receiver).sendMessage(Chat.color(config.getString(message)));
                return;
            }
            ((ConsoleCommandSender) receiver).sendMessage(Chat.color(config.getString(message).replace("<PLAYER>", replace)));
        }
    }
}
