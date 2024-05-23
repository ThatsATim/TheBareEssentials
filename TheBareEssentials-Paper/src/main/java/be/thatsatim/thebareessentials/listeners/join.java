package be.thatsatim.thebareessentials.listeners;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class join implements Listener {

    FileConfiguration config;

    public join(TheBareEssentials plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        config = plugin.getConfig();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            event.setJoinMessage(null);
            Bukkit.broadcastMessage(Chat.color(config.getString("firstJoinMessage").replace("<PLAYER>", player.getDisplayName())));
            return;
        }

        event.setJoinMessage(null);
        Bukkit.broadcastMessage(Chat.color(config.getString("joinMessage").replace("<PLAYER>", player.getDisplayName())));
    }
}
