package be.thatsatim.thebareessentials.commands;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
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
            Chat.message(sender, "heal.messages.wrongArguments", config, Chat.noReplacements);
            return true;
        }

        if (arguments.length == 0) {
            if (!(sender instanceof Player)) {
                Chat.message(sender, "heal.messages.wrongArguments", config, Chat.noReplacements);
                return true;
            }
            Chat.message(sender, "heal.messages.self", config, Chat.noReplacements);
            heal((Player) sender);
            return true;
        }

        if ((Bukkit.getPlayerExact(arguments[0]) == null)) {
            Chat.message(sender, "playerNotFound", config, Chat.noReplacements);
            return true;
        }

        Player player = Bukkit.getPlayerExact(arguments[0]);
        assert player != null;

        heal(player);

        String[][] replacementsSender = {{"<PLAYER>", String.valueOf(player.displayName())}};

        if (sender instanceof Player) {
            String[][] replacementsPlayer = {{"<PLAYER>", String.valueOf(((Player) sender).displayName())}};
            Chat.message(player, "heal.messages.other.player", config, replacementsPlayer);
            Chat.message(sender, "heal.messages.other.sender", config, replacementsSender);
            return true;
        }

        Chat.message(player, "heal.messages.console", config, Chat.noReplacements);
        Chat.message(sender, "heal.messages.other.sender", config, replacementsSender);
        return true;
    }

    public void heal(Player player) {
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
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
