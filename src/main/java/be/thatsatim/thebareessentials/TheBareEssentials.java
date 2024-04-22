package be.thatsatim.thebareessentials;

import be.thatsatim.thebareessentials.commands.*;
import be.thatsatim.thebareessentials.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class TheBareEssentials extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        new join(this);
        new leave(this);
        new Fly(this);
        new Top(this);
        new Repair(this);
        new Gamemode(this);
        new GmX(this);
        new Heal(this);
        new TpX(this);
        new Tpa(this);
        new be.thatsatim.thebareessentials.commands.TheBareEssentials(this);
    }
}
