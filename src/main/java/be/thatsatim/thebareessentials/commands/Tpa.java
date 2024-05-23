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
import org.bukkit.entity.Player;
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
            Player playerReceiver = (Player) sender;
            if (!(targetMap.containsValue(playerReceiver.getUniqueId()))) {
                Chat.message(playerReceiver, "tpa.noRequest", config, Chat.noReplacements);
                return true;
            }
            for (Map.Entry<UUID, UUID> entry : targetMap.entrySet()) {
                String[][] replacementsReceiver = {{"<PLAYER>", Bukkit.getPlayer(entry.getKey()).getDisplayName()}};
                String[][] replacementsRequester = {{"<PLAYER>", playerReceiver.getDisplayName()}};
                if ((entry.getValue()).equals(playerReceiver.getUniqueId())) {
                    Chat.message(playerReceiver, "tpa.tpDeny.receiver", config, replacementsReceiver);
                    Chat.message(Bukkit.getPlayer(entry.getKey()), "tpa.tpDeny.requester", config, replacementsRequester);
                    targetMap.remove(entry.getKey());
                    break;
                }
            }
            return true;
        }

        if (label.equalsIgnoreCase("tpaccept")) {
            Player playerReceiver = (Player) sender;
            if (!(targetMap.containsValue(playerReceiver.getUniqueId()))) {
                Chat.message(playerReceiver, "tpa.noRequest", config, Chat.noReplacements);
                return true;
            }
            for (Map.Entry<UUID, UUID> entry : targetMap.entrySet()) {
                if ((entry.getValue()).equals(playerReceiver.getUniqueId())) {

                    Player playerRequester = Bukkit.getPlayer(entry.getKey());

                    SuccessfulTpaEvent event = new SuccessfulTpaEvent(playerReceiver, playerReceiver.getLocation());
                    Bukkit.getPluginManager().callEvent(event);

                    playerRequester.teleport(playerReceiver);
                    targetMap.remove(entry.getKey());

                    String[][] replacementsReceiver = {{"<PLAYER>", playerRequester.getDisplayName()}};
                    String[][] replacementsRequester = {{"<PLAYER>", playerReceiver.getDisplayName()}};

                    Chat.message(playerReceiver, "tpa.tpAccept.receiver", config, replacementsReceiver);
                    Chat.message(playerRequester, "tpa.tpAccept.requester", config, replacementsRequester);
                    break;
                }
            }
            return true;
        }

        if (!(arguments.length == 1)) {
            Chat.message(sender, "tpa.wrongArguments", config, Chat.noReplacements);
            return true;
        }

        if (Bukkit.getPlayerExact(arguments[0]) == null) {
            Chat.message(sender, "playerNotFound", config, Chat.noReplacements);
            return true;
        }

        Player playerRequester = (Player) sender;
        Player playerReceiver = Bukkit.getPlayer(arguments[0]);

        if (playerRequester == playerReceiver) {
            Chat.message(playerRequester, "tpa.tpaToSelf", config, Chat.noReplacements);
            return true;
        }

        if (targetMap.containsKey(playerRequester.getUniqueId())) {
            String[][] replacements = {{"<PLAYER>", playerReceiver.getDisplayName()}};
            Chat.message(playerRequester, "tpa.requestPending", config, replacements);
            return true;
        }

        targetMap.put(playerRequester.getUniqueId(), playerReceiver.getUniqueId());

        String[][] replacementsRequester = {{"<PLAYER>", playerReceiver.getDisplayName()}};
        Chat.message(playerRequester, "tpa.requestSent.requester", config, replacementsRequester);

        String[][] replacementsReceiver = {{"<PLAYER>", playerReceiver.getDisplayName()}};
        Chat.message(playerReceiver, "tpa.requestSent.receiver", config, replacementsReceiver);

        (new BukkitRunnable() {
            public void run() {
                Tpa.targetMap.remove(playerRequester.getUniqueId());
                String[][] replacementsRequester = {{"<PLAYER>", playerReceiver.getDisplayName()}};
                String[][] replacementsReceiver = {{"<PLAYER>", playerRequester.getDisplayName()}};
                Chat.message(playerRequester, "tpa.expire", config, replacementsRequester);
                Chat.message(playerReceiver, "tpa.expire", config, replacementsReceiver);
            }
        }).runTaskLaterAsynchronously(this.plugin, 6000L);

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