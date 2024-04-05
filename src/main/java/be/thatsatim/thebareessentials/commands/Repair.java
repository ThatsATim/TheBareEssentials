package be.thatsatim.thebareessentials.commands;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.utils.Chat;
import com.google.common.xml.XmlEscapers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Repair implements CommandExecutor, TabCompleter {

    FileConfiguration config;

    public Repair(TheBareEssentials plugin){
        plugin.getCommand("repair").setExecutor(this);
        config = plugin.getConfig();
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] arguments) {

        if (!(sender.hasPermission("TBE.repair"))) {
            Chat.message(sender, "no_perms", config, null);
            return true;
        }

        if (arguments.length > 2 || arguments.length == 0) {
            Chat.message(sender, "repair.messages.wrongArguments", config, null);
            return true;
        }

        if (arguments.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Chat.color("&6TheBareEssentials: &c/repair <HeldItem / all> <playername>"));
                return true;
            }
            Player player = (Player) sender;
            if (arguments[0].equalsIgnoreCase("HeldItem")) {
                repairItem(player.getInventory().getItemInMainHand());
                Chat.message(player, "repair.messages.self.HeldItem", config, null);
                return true;
            }
            repairInventory(player.getInventory());
            Chat.message(player, "repair.messages.self.all", config, null);
            return true;
        }

        Player player = Bukkit.getPlayerExact(arguments[1]);
        assert player != null;

        if (arguments[0].equalsIgnoreCase("HeldItem")) {
            repairItem(player.getInventory().getItemInMainHand());
            if (sender instanceof Player) {
                Chat.message(player, "repair.messages.other.HeldItem.player", config, ((Player) sender).getDisplayName());
                Chat.message(sender, "repair.messages.other.HeldItem.sender", config, player.getDisplayName());
                return true;
            }
            Chat.message(player, "repair.messages.other.handheld.console", config, null);
            Chat.message(sender, "repair.messages.other.HeldItem.sender", config, player.getDisplayName());
            return true;
        }
        repairInventory(player.getInventory());
        if (sender instanceof Player) {
            Chat.message(player, "repair.messages.other.all.player", config, ((Player) sender).getDisplayName());
            Chat.message(sender, "repair.messages.other.all.sender", config, player.getDisplayName());
            return true;
        }
        Chat.message(player, "repair.messages.other.all.console", config, null);
        Chat.message(sender, "repair.messages.other.all.sender", config, player.getDisplayName());
        return true;
    }

    public void repairItem(ItemStack item) {
        item.setDurability((short) 0);
    }

    public void repairInventory(PlayerInventory inventory) {
        for (ItemStack item : inventory) {
            if (item == null) continue;
            if (!item.hasItemMeta()) continue;
            if (item.getItemMeta() instanceof Repairable) {
                item.setDurability((short) 0);
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (arguments.length == 1) {
            List<String> options = Arrays.asList("HeldItem", "all");
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