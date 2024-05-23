package be.thatsatim.thebareessentials.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Chat {
    public static String color (String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String[][] noReplacements = {};

    public static void message(Object receiver, String message, FileConfiguration config, String[][] replacements) {

        message = config.getString(message);
        for (String[] replacement : replacements) {
            message = message.replace(replacement[0], replacement[1]);
        }
        message = color(message);

        if (receiver instanceof Player) {
            ((Player) receiver).sendMessage(message);
            return;
        }
        if (receiver instanceof ConsoleCommandSender) {
            ((ConsoleCommandSender) receiver).sendMessage(message);
        }
    }
}