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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Gamemode implements CommandExecutor, TabCompleter {

    FileConfiguration config;

    public Gamemode(TheBareEssentials plugin){
        plugin.getCommand("gamemode").setExecutor(this);
        config = plugin.getConfig();
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] arguments) {

        if (!(sender.hasPermission("TBE.gamemode"))) {
            Chat.message(sender, "no_perms", config, null);
            return true;
        }

        if (arguments.length == 0 || arguments.length > 2) {
            // TODO add wrong args message
            Chat.message(sender, "", config, null);
            return true;
        }

        if (arguments.length == 1) {
            if (!(sender instanceof Player)) {
                // TODO add wrongArguments message
                Chat.message(sender, "", config, null);
                return true;
            }
            gamemode((Player) sender, arguments[0]);
            // TODO add message
            return true;
        }

        Player player = Bukkit.getPlayerExact(arguments[1]);
        assert player != null;

        gamemode((Player) sender, arguments[0]);
        // TODO add message
        return true;
    }

    public void gamemode(Player player, String mode) {
        switch(mode) {
            case "survival": case "0": case "s":
                PlayerState.changeGamemode(player, "SURVIVAL");
                break;
            case "creative": case "1": case "c":
                PlayerState.changeGamemode(player, "CREATIVE");
                break;
            case "adventure": case "2": case "a":
                PlayerState.changeGamemode(player, "ADVENTURE");
                break;
            case "spectator": case "3": case "sp":
                PlayerState.changeGamemode(player, "SPECTATOR");
                break;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (arguments.length == 1) {
            List<String> options = Arrays.asList("survival", "creative", "spectator", "adventure");
            List<String> result = new ArrayList<>(Collections.emptyList());
            for (String option : options) {
                if (option.toLowerCase().startsWith(arguments[0].toLowerCase())) {
                    result.add(option);
                }
            }
            return result;
        }

        if (arguments.length == 2) {
            return null;
        }

        return Collections.emptyList();
    }
}