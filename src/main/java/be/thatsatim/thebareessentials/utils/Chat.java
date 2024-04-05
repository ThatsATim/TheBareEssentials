package be.thatsatim.thebareessentials.utils;

import org.bukkit.ChatColor;

public class Chat {
    public static String color (String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
