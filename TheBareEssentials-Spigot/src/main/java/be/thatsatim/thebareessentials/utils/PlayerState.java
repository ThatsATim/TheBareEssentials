package be.thatsatim.thebareessentials.utils;

import org.bukkit.GameMode;

public class PlayerState {
    public static void changeGamemode(org.bukkit.entity.Player player, String mode) {
        player.setGameMode(GameMode.valueOf(mode));
    }
}