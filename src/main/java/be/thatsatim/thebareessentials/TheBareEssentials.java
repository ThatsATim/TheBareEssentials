package be.thatsatim.thebareessentials;

import be.thatsatim.thebareessentials.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class TheBareEssentials extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        new Fly(this);
        new Top(this);
        new Repair(this);
        new Gamemode(this);
        new GmX(this);
        new Heal(this);
        new TpX(this);
        new Tpa(this);
    }
}
