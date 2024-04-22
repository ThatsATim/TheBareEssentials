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
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class Heal implements CommandExecutor, TabCompleter {

    FileConfiguration config;

    public Heal(TheBareEssentials plugin) {
        plugin.getCommand("heal").setExecutor(this);
        config = plugin.getConfig();
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!(sender.hasPermission("TBE.heal"))) {
            Chat.message(sender, "no_perms", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length > 1) {
            // TODO add wrong args message
            Chat.message(sender, "", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length == 0) {
            if (!(sender instanceof Player)) {
                // TODO add wrong args message
                Chat.message(sender, "", config, Chat.noReplacements);
                return true;
            }
            // TODO add message
            heal((Player) sender);
            return true;
        }

        Player player = Bukkit.getPlayerExact(arguments[0]);
        assert player != null;

        // TODO add message
        heal(player);

        return true;
    }

    public void heal(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setFireTicks(0);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
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