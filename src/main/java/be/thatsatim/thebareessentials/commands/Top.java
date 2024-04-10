package be.thatsatim.thebareessentials.commands;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.utils.Chat;
import it.unimi.dsi.fastutil.ints.IntLists;
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

public class Top implements CommandExecutor, TabCompleter {

    FileConfiguration config;

    public Top(TheBareEssentials plugin){
        plugin.getCommand("top").setExecutor(this);
        config = plugin.getConfig();
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!(sender.hasPermission("TBE.top"))) {
            Chat.message(sender, "no_perms", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length > 1) {
            Chat.message(sender, "top.messages.wrongArguments", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Chat.color("&6TheBareEssentials: &c/top <playername>"));
                return true;
            }
            Player player = (Player) sender;
            teleportToTop(player);
            Chat.message(player, "top.messages.self", config, Chat.noReplacements);
            return true;
        }

        if (Bukkit.getPlayerExact(arguments[0]) == null) {
            Chat.message(sender, "playerNotFound", config, Chat.noReplacements);
            return true;
        }

        Player player = Bukkit.getPlayer(arguments[0]);
        assert player != null;

        String[][] replacementsSender = {{"<PLAYER>", player.getDisplayName()}};
        String[][] replacementsPlayer = {{"<PLAYER>", ((Player) sender).getDisplayName()}};

        teleportToTop(player);

        if (sender == Bukkit.getPlayerExact(arguments[0])) {
            Chat.message(sender, "top.messages.self", config, Chat.noReplacements);
            return true;
        }

        if (sender instanceof Player) {
            Chat.message(sender, "top.messages.other.sender", config, replacementsSender);
            Chat.message(player, "top.messages.other.player", config, replacementsPlayer);
            return true;
        }
        Chat.message(sender, "top.messages.other.sender", config, replacementsSender);
        Chat.message(player, "top.messages.console", config, Chat.noReplacements);
        return true;
    }

    public void teleportToTop(Player player) {
        player.teleport(player.getWorld().getHighestBlockAt(player.getLocation()).getLocation().add(0, 1, 0));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {
        if (arguments.length == 1) {
            return null;
        }
        return Collections.emptyList();
    }
}