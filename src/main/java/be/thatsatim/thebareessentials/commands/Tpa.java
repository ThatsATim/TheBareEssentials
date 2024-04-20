package be.thatsatim.thebareessentials.commands;

import be.thatsatim.thebareessentials.TheBareEssentials;
import be.thatsatim.thebareessentials.events.SuccessfulTpaEvent;
import be.thatsatim.thebareessentials.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Tpa implements CommandExecutor, TabCompleter {

    FileConfiguration config;
    private TheBareEssentials plugin;

    public Tpa(TheBareEssentials plugin) {
        this.plugin = plugin;
        plugin.getCommand("tpa").setExecutor(this);
        plugin.getCommand("tpaccept").setExecutor(this);
        plugin.getCommand("tpdeny").setExecutor(this);
        config = plugin.getConfig();
    }

    static HashMap<UUID, UUID> targetMap = new HashMap<>();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!(sender.hasPermission("TBE.tpa"))) {
            Chat.message(sender, "no_perms", config, Chat.noReplacements);
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("&6TheBareEssentials: &cConsole can't teleport");
            return true;
        }

        if (label.equalsIgnoreCase("tpdeny")) {
            Player playerSender = (Player) sender;
            if (!(targetMap.containsValue(playerSender.getUniqueId()))) {
                // TODO add no request message
                return true;
            }
            for (Map.Entry<UUID, UUID> entry : targetMap.entrySet()) {
                if (((UUID)entry.getValue()).equals(playerSender.getUniqueId())) {
                    targetMap.remove(entry.getKey());
                    Player playerTarget = Bukkit.getPlayer(entry.getKey());
                    // TODO add messages
                    break;
                }
            }
            return true;
        }

        if (label.equalsIgnoreCase("tpaccept")) {
            Player playerSender = (Player) sender;
            if (targetMap.containsValue(playerSender.getUniqueId())) {
                // TODO add no request message
                return true;
            }
            for (Map.Entry<UUID, UUID> entry : targetMap.entrySet()) {
                if (((UUID)entry.getValue()).equals(playerSender.getUniqueId())) {

                    Player playerTarget = Bukkit.getPlayer(entry.getKey());

                    SuccessfulTpaEvent event = new SuccessfulTpaEvent(playerSender, playerSender.getLocation());
                    Bukkit.getPluginManager().callEvent(event);

                    playerTarget.teleport((Entity)playerSender);
                    targetMap.remove(entry.getKey());
                    break;
                }
            }
            return true;
        }

        if (!(arguments.length == 1)) {
            // TODO add wrong arguments message
            return true;
        }

        if (Bukkit.getPlayerExact(arguments[0]) == null) {
            Chat.message(sender, "playerNotFound", config, Chat.noReplacements);
            return true;
        }

        Player playerSender = (Player) sender;
        Player playerTarget = Bukkit.getPlayer(arguments[0]);

        if (playerSender == playerTarget) {
            // TODO add message for tpa to self
            return true;
        }

        if (targetMap.containsKey(playerSender.getUniqueId())) {
            // TODO add message player already has a request
            return true;
        }

        targetMap.put(playerSender.getUniqueId(), playerTarget.getUniqueId());

        (new BukkitRunnable() {
            public void run() {
                Tpa.targetMap.remove(playerSender.getUniqueId());
            }
        }).runTaskLaterAsynchronously((Plugin)this.plugin, 6000L);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {
        if (label.equalsIgnoreCase("tpa") && arguments.length == 1) {
            return null;
        }
        return Collections.emptyList();
    }
}