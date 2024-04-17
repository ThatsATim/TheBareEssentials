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

    public TpX(TheBareEssentials plugin){
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

            if (arguments.length == 0) {
                // TODO Add wrong args message
                return true;
            }

            Player player = (Player) sender;

            if ((Bukkit.getPlayerExact(arguments[0]) == null)) {
                Chat.message(sender, "playerNotFound", config, Chat.noReplacements);
                return true;
            }

            Player target = Bukkit.getPlayer(arguments[0]);
            target.teleport(player.getLocation());
            // TODO add messages
            return true;
        }

        if (!(sender.hasPermission("TBE.tpall"))) {
            Chat.message(sender, "no_perms", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length == 0) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("&6TbeBareEssentials: &c/tpall PlayerName");
                return true;
            }

            Player player = (Player) sender;

            for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                target.teleport(player.getLocation());
                // TODO add message
            }
            return true;
        }

        if ((Bukkit.getPlayerExact(arguments[0]) == null)) {
            Chat.message(sender, "playerNotFound", config, Chat.noReplacements);
            return true;
        }

        Player player = Bukkit.getPlayer(arguments[0]);

        for (Player target : Bukkit.getServer().getOnlinePlayers()) {
            target.teleport(player.getLocation());
            // TODO add messages
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