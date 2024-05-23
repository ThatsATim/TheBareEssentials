package be.thatsatim.thebareessentials.commands;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.utils.Chat;
import be.thatsatim.thebareessentials.utils.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
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
            Chat.message(sender, "no_perms", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length > 1) {
            Chat.message(sender, "gmX.messages.wrongArguments", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length == 0) {
            if (!(sender instanceof Player)) {
                Chat.message(sender, "gmX.messages.wrongArguments", config, Chat.noReplacements);
                return true;
            }
            String mode = gamemode((Player) sender, label);
            String[][] replacements = {{"<MODE>", mode}};
            Chat.message(sender, "gamemode.messages.self", config, replacements);
            return true;
        }

        if ((Bukkit.getPlayerExact(arguments[0]) == null)) {
            Chat.message(sender, "playerNotFound", config, Chat.noReplacements);
            return true;
        }

        Player player = Bukkit.getPlayerExact(arguments[0]);
        assert player != null;

        String mode = gamemode(player, label);
        String[][] replacementsSender = {{"<MODE>", mode}, {"<PLAYER>", String.valueOf(player.displayName())}};
        Chat.message(sender, "gamemode.messages.other.sender", config, replacementsSender);
        if (sender instanceof ConsoleCommandSender) {
            String[][] replacementsConsole = {{"<MODE>", mode}};
            Chat.message(player, "gamemode.messages.console", config, replacementsConsole);
            return true;
        }
        String[][] replacementsPlayer = {{"<MODE>", mode}, {"<PLAYER>", String.valueOf(((Player) sender).displayName())}};
        Chat.message(player, "gamemode.messages.other.target", config, replacementsPlayer);
        return true;
    }

    public String gamemode(Player player, String mode) {
        switch(mode) {
            case "gmc":
                PlayerState.changeGamemode(player, "CREATIVE");
                return "creative";
            case "gms":
                PlayerState.changeGamemode(player, "SURVIVAL");
                return "survival";
            case "gma":
                PlayerState.changeGamemode(player, "ADVENTURE");
                return "adventure";
            case "gmsp":
                PlayerState.changeGamemode(player, "SPECTATOR");
                return "spectator";
        }
        return mode;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {
        if (arguments.length == 1) {
            return null;
        }
        return Collections.emptyList();
    }
}
