package be.thatsatim.thebareessentials;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import be.thatsatim.thebareessentials.commands.Fly;

public final class TheBareEssentials extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        new Fly(this);
    }

}
