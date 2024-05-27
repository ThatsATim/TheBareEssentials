package be.thatsatim.thebareessentials.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Chat {
    public static String color (String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String[][] noReplacements = {};

    private static String getFromConfig(String message, FileConfiguration config, String[][] replacements) {
        message = config.getString(message);
        for (String[] replacement : replacements) {
            assert message != null;
            message = message.replace(replacement[0], replacement[1]);
        }

        try {
            assert message != null;
            message = message.replace("\\n", "\n");
        } catch (Exception ignored) {
        }

        return color(message);
    }

    public static void message(Object receiver, String message, FileConfiguration config, String[][] replacements) {
        message = getFromConfig(message, config, replacements);
        if (receiver instanceof Player) {
            ((Player) receiver).sendMessage(message);
            return;
        }
        if (receiver instanceof ConsoleCommandSender) {
            ((ConsoleCommandSender) receiver).sendMessage(message);
        }
    }

    public static void broadcast(String message, FileConfiguration config, String[][] replacements) {
        message = getFromConfig(message, config, replacements);
        final Component component = Component.text(message);
        Bukkit.broadcast(component, Server.BROADCAST_CHANNEL_USERS);
    }

}