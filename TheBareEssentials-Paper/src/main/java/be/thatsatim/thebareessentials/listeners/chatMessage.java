package be.thatsatim.thebareessentials.listeners;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.utils.Chat;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class chatMessage implements Listener {

    public chatMessage(TheBareEssentials plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (!(player.hasPermission("TBE.chatColor"))) {
            return;
        }
        TextComponent textComponent = (TextComponent) event.message();
        String message = textComponent.content();

        message =  Chat.color(message);

        final Component component = Component.text(message);
        event.message(component);
    }

}