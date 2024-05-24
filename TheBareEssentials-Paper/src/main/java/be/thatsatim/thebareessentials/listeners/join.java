package be.thatsatim.thebareessentials.listeners;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.utils.Chat;
import net.kyori.adventure.text.TextComponent;
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

        TextComponent textComponent = (TextComponent) player.displayName();
        String displayName = textComponent.content();

        String[][] replacements = {{"<PLAYER>", displayName}};

        event.joinMessage(null);

        if (!player.hasPlayedBefore()) {
            Chat.broadcast("firstJoinMessage", config, replacements);
            return;
        }

        Chat.broadcast("joinMessage", config, replacements);
    }
}
