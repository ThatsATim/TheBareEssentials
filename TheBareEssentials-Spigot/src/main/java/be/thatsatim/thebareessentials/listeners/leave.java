package be.thatsatim.thebareessentials.listeners;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class leave implements Listener {
    FileConfiguration config;

    public leave(TheBareEssentials plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        config = plugin.getConfig();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        event.setQuitMessage(null);
        Bukkit.broadcastMessage(Chat.color(config.getString("leaveMessage").replace("<PLAYER>", player.getDisplayName())));
    }
}
