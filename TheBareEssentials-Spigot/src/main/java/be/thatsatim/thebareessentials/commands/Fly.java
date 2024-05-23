package be.thatsatim.thebareessentials.commands;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class Fly implements CommandExecutor, TabCompleter {

    FileConfiguration config;

    public Fly(TheBareEssentials plugin) {
        plugin.getCommand("fly").setExecutor(this);
        config = plugin.getConfig();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {

        if (!(sender.hasPermission("TBE.fly"))) {
            Chat.message(sender, "no_perms", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length > 1) {
            Chat.message(sender, "fly.messages.wrongArguments", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Chat.color("&6TheBareEssentials: &c/fly <playername>"));
                return true;
            }
            int active = toggleFly((Player) sender);
            flySelfMessage((Player) sender, active);
            return true;
        }

        if ((Bukkit.getPlayerExact(arguments[0]) == null)) {
            Chat.message(sender, "playerNotFound", config, Chat.noReplacements);
            return true;
        }

        Player player = Bukkit.getPlayer(arguments[0]);
        assert player != null;
        int active = toggleFly(player);

        if (sender == Bukkit.getPlayerExact(arguments[0])) {
            flySelfMessage((Player) sender, active);
            return true;
        }

        String[][] replacementsSender = {{"<PLAYER>", player.getDisplayName()}};

        if (sender instanceof Player) {
            String[][] replacementsPlayer = {{"<PLAYER>", ((Player) sender).getDisplayName()}};
            if (active == 1) {
                Chat.message(sender, "fly.messages.other.sender.activate", config, replacementsSender);
                Chat.message(player, "fly.messages.other.target.activate", config, replacementsPlayer);
                return true;
            }
            Chat.message(sender, "fly.messages.other.sender.deactivate", config, replacementsSender);
            Chat.message(player, "fly.messages.other.target.deactivate", config, replacementsPlayer);
            return true;
        }

        if (active == 1) {
            Chat.message(sender, "fly.messages.other.sender.activate", config, replacementsSender);
            Chat.message(player, "fly.messages.console.target.activate", config, Chat.noReplacements);
            return true;
        }
        Chat.message(sender, "fly.messages.other.sender.deactivate", config, replacementsSender);
        Chat.message(player, "fly.messages.console.target.deactivate", config, Chat.noReplacements);
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

    public void flySelfMessage(Player receiver, Integer active) {
        if (active == 1) {
            Chat.message(receiver, "fly.messages.self.activate", config, Chat.noReplacements);
            return;
        }
        Chat.message(receiver, "fly.messages.self.deactivate", config,Chat.noReplacements);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length == 1) {
            return null;
        }
        return Collections.emptyList();
    }
}