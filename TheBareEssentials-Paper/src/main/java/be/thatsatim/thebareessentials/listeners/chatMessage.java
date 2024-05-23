package be.thatsatim.thebareessentials.listeners;

import be.thatsatim.thebareessentials.TheBareEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class chatMessage implements Listener {

    public chatMessage(TheBareEssentials plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!(player.hasPermission("TBE.chatColor"))) {
            return;
        }
        event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
    }

}
