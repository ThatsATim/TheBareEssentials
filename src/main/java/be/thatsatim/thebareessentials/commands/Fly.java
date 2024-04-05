package be.thatsatim.thebareessentials.commands;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.utils.Chat;
import com.sun.org.apache.bcel.internal.generic.ObjectType;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class Fly implements CommandExecutor, TabCompleter {

    FileConfiguration config;

    public Fly(TheBareEssentials plugin){
        plugin.getCommand("fly").setExecutor(this);
        config = plugin.getConfig();
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] arguments) {

        if (arguments.length > 1) {
            sender.sendMessage(Chat.color(config.getString("fly.messages.tooManyArguments")));
            return true;
        }

        if (arguments.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Chat.color("&6TheBareEssentials: &c/fly playername"));
                return true;
            }
            int active = toggleFly((Player) sender);
            flySelfMessage((Player) sender, active);
            return true;
        }

        if ((Bukkit.getPlayerExact(arguments[0]) == null)) {
            message(sender, "playerNotFound", null);
            return true;
        }

        Player player = Bukkit.getPlayer(arguments[0]);
        assert player != null;
        int active = toggleFly(player);

        if (sender == Bukkit.getPlayerExact(arguments[0])) {
            flySelfMessage((Player) sender, active);
        }

        if (sender instanceof Player) {
            if (active == 1) {
                message(sender, "fly.messages.other.sender.activate", player.getDisplayName());
                message(player, "fly.messages.other.target.activate", ((Player) sender).getDisplayName());
                return true;
            }
            message(sender, "fly.messages.other.sender.deactivate", player.getDisplayName());
            message(player, "fly.messages.other.target.deactivate", ((Player) sender).getDisplayName());
            return true;
        }

        if (active == 1) {
            message(sender, "fly.messages.other.sender.activate", player.getDisplayName());
            message(player, "fly.messages.console.target.activate", null);
            return true;
        }
        message(sender, "fly.messages.other.sender.deactivate", player.getDisplayName());
        message(player, "fly.messages.console.target.deactivate", null);
        return true;
    }

    // The bit is used to track the status => 0 = fly has been disabled & 1 = fly has been enabled
    public int toggleFly(Player player) {
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            return 0;
        }
        player.setAllowFlight(true);
        return 1;
    }

    public void message(Object receiver, String message, String replace) {
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

    public void flySelfMessage(Player receiver, Integer active) {
        if (active == 1) {
            message(receiver, "fly.messages.self.activate", null);
            return;
        }
        message(receiver, "fly.messages.self.deactivate", null);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {
        if (arguments.length == 1) {
            return null;
        }
        return Collections.emptyList();
    }
}