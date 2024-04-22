package be.thatsatim.thebareessentials.commands;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.utils.Chat;
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

public class TpX implements CommandExecutor, TabCompleter {

    FileConfiguration config;

    public TpX(TheBareEssentials plugin) {
        plugin.getCommand("tpall").setExecutor(this);
        plugin.getCommand("tphere").setExecutor(this);
        config = plugin.getConfig();
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (label.equalsIgnoreCase("tphere")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("&6TheBareEssentials: &c/tphere is not for console use");
                return true;
            }

            if (!(sender.hasPermission("TBE.tphere"))) {
                Chat.message(sender, "no_perms", config, Chat.noReplacements);
                return true;
            }

            if (arguments.length != 1) {
                Chat.message(sender, "tphere.wrongArguments", config, Chat.noReplacements);
                return true;
            }

            Player player = (Player) sender;

            if ((Bukkit.getPlayerExact(arguments[0]) == null)) {
                Chat.message(sender, "playerNotFound", config, Chat.noReplacements);
                return true;
            }

            Player target = Bukkit.getPlayer(arguments[0]);
            target.teleport(player.getLocation());

            String[][] replacementsSender = {{"<PLAYER>", player.getDisplayName()}};
            String[][] replacementsTarget = {{"<PLAYER>", ((Player) sender).getDisplayName()}};

            Chat.message(player, "tphere.sender", config, replacementsSender);
            Chat.message(target, "tphere.target", config, replacementsTarget);
            return true;
        }

        if (!(sender.hasPermission("TBE.tpall"))) {
            Chat.message(sender, "no_perms", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length >= 2) {
            Chat.message(sender, "tpall.wrongArguments", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length == 0) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("&6TbeBareEssentials: &c/tpall PlayerName");
                return true;
            }

            Player player = (Player) sender;
            String[][] replacements = {{"<PLAYER>", player.getDisplayName()}};

            Chat.message(player, "tpall.toSelf.sender", config, Chat.noReplacements);

            for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                target.teleport(player.getLocation());
                Chat.message(target, "tpall.toSelf.players", config, replacements);
            }
            return true;
        }

        if ((Bukkit.getPlayerExact(arguments[0]) == null)) {
            Chat.message(sender, "playerNotFound", config, Chat.noReplacements);
            return true;
        }

        Player player = Bukkit.getPlayer(arguments[0]);
        String[][] replacementsTarget = {{"<PLAYER>", ((Player) sender).getDisplayName()}};
        String[][] replacementsSender = {{"<PLAYER>", player.getDisplayName()}};
        String[][] replacementsPlayers = {
                {"<PLAYER>", ((Player) sender).getDisplayName()},
                {"<TARGET>", player.getDisplayName()}
        };

        Chat.message(player, "tpall.toOtherPlayer.target", config, replacementsTarget);
        Chat.message(sender, "tpall.toOtherPlayer.sender", config, replacementsSender);

        for (Player target : Bukkit.getServer().getOnlinePlayers()) {
            target.teleport(player.getLocation());
            Chat.message(target, "tpall.toOtherPlayer.players", config, replacementsPlayers);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {
        if (arguments.length == 1) {
            return null;
        }
        return Collections.emptyList();
    }
}