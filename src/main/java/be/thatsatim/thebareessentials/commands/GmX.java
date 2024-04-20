package be.thatsatim.thebareessentials.commands;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.utils.Chat;
import be.thatsatim.thebareessentials.utils.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class GmX implements CommandExecutor, TabCompleter {

    FileConfiguration config;

    public GmX(TheBareEssentials plugin) {
        plugin.getCommand("gmc").setExecutor(this);
        plugin.getCommand("gms").setExecutor(this);
        plugin.getCommand("gma").setExecutor(this);
        plugin.getCommand("gmsp").setExecutor(this);
        config = plugin.getConfig();
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!(sender.hasPermission("TBE.gamemode"))) {
            Chat.message(sender, "no_perms", config, null);
            return true;
        }

        if (arguments.length > 1) {
            // TODO add wrong args message
            Chat.message(sender, "", config, null);
            return true;
        }

        if (arguments.length == 0) {
            if (!(sender instanceof Player)) {
                // TODO add wrongArguments message
                Chat.message(sender, "", config, null);
                return true;
            }
            gamemode((Player) sender, label);
            return true;
        }

        Player player = Bukkit.getPlayerExact(arguments[0]);
        assert player != null;

        gamemode((Player) sender, arguments[0]);
        // TODO add message

        return true;
    }

    public void gamemode(Player player, String mode) {
        switch(mode) {
            case "gmc":
                PlayerState.changeGamemode(player, "CREATIVE");
                player.sendMessage(mode);
                break;
            case "gms":
                PlayerState.changeGamemode(player, "SURVIVAL");
                player.sendMessage(mode);
                break;
            case "gma":
                PlayerState.changeGamemode(player, "ADVENTURE");
                player.sendMessage(mode);
                break;
            case "gmsp":
                PlayerState.changeGamemode(player, "SPECTATOR");
                player.sendMessage(mode);
                break;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (arguments.length == 1) {
            return null;
        }
        return Collections.emptyList();
    }
}
