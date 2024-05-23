package be.thatsatim.thebareessentials.commands;

import be.thatsatim.thebareessentials.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TheBareEssentials implements CommandExecutor, TabCompleter {

    private be.thatsatim.thebareessentials.TheBareEssentials plugin;

    public TheBareEssentials(be.thatsatim.thebareessentials.TheBareEssentials plugin) {
        this.plugin = plugin;
        plugin.getCommand("thebareessentials").setExecutor(this);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (arguments.length == 0 || arguments[0].equalsIgnoreCase("version")) {
            sender.sendMessage(Chat.color("&6TheBareEssentials, version 1.0"));
            return true;
        }

        if (arguments[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(Chat.color("&6TheBareEssentials config has been reloaded"));
            return true;
        }

        if (arguments.length > 1) {
            sender.sendMessage(Chat.color("&6TheBareEssentials: &cWrong arguments => /TheBareEssentials <reload / version>"));
            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (arguments.length == 1) {
            List<String> options = Arrays.asList("version", "reload");
            List<String> result = new ArrayList<>(Collections.emptyList());
            for (String option : options) {
                if (option.toLowerCase().startsWith(arguments[0].toLowerCase())) {
                    result.add(option);
                }
            }
            return result;
        }

        return Collections.emptyList();
    }
}
