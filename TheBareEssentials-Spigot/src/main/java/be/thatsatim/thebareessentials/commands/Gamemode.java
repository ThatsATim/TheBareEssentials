package be.thatsatim.thebareessentials.commands;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.utils.Chat;
import be.thatsatim.thebareessentials.utils.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class Gamemode implements CommandExecutor, TabCompleter {

    FileConfiguration config;

    public Gamemode(TheBareEssentials plugin) {
        plugin.getCommand("gamemode").setExecutor(this);
        config = plugin.getConfig();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {

        if (!(sender.hasPermission("TBE.gamemode"))) {
            Chat.message(sender, "no_perms", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length == 0 || arguments.length > 2) {
            Chat.message(sender, "gamemode.messages.wrongArguments", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length == 1) {
            if (!(sender instanceof Player)) {
                Chat.message(sender, "gamemode.messages.wrongArguments", config, Chat.noReplacements);
                return true;
            }
            String mode = gamemode((Player) sender, arguments[0]);

            if (mode.equalsIgnoreCase("wrongArgs")) {
                Chat.message(sender, "gamemode.messages.wrongArguments", config, Chat.noReplacements);
                return true;
            }

            String[][] replacements = {{"<MODE>", mode}};
            Chat.message(sender, "gamemode.messages.self", config, replacements);
            return true;
        }

        if ((Bukkit.getPlayerExact(arguments[1]) == null)) {
            Chat.message(sender, "playerNotFound", config, Chat.noReplacements);
            return true;
        }

        Player player = Bukkit.getPlayerExact(arguments[1]);
        assert player != null;

        String mode = gamemode(player, arguments[0]);

        if (mode.equalsIgnoreCase("wrongArgs")) {
            Chat.message(sender, "gamemode.messages.wrongArguments", config, Chat.noReplacements);
            return true;
        }

        String[][] replacementsSender = {{"<MODE>", mode}, {"<PLAYER>", player.getDisplayName()}};
        Chat.message(sender, "gamemode.messages.other.sender", config, replacementsSender);
        if (sender instanceof ConsoleCommandSender) {
            String[][] replacementsConsole = {{"<MODE>", mode}};
            Chat.message(player, "gamemode.messages.console", config, replacementsConsole);
            return true;
        }
        String[][] replacementsPlayer = {{"<MODE>", mode}, {"<PLAYER>",((Player) sender).getDisplayName()}};
        Chat.message(player, "gamemode.messages.other.target", config, replacementsPlayer);
        return true;
    }

    public String gamemode(Player player, String mode) {
        switch(mode) {
            case "survival": case "0": case "s":
                PlayerState.changeGamemode(player, "SURVIVAL");
                return "survival";
            case "creative": case "1": case "c":
                PlayerState.changeGamemode(player, "CREATIVE");
                return "creative";
            case "adventure": case "2": case "a":
                PlayerState.changeGamemode(player, "ADVENTURE");
                return "adventure";
            case "spectator": case "3": case "sp":
                PlayerState.changeGamemode(player, "SPECTATOR");
                return "spectator";
        }
        return "wrongArgs";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {

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