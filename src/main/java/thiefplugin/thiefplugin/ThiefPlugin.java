package thiefplugin.thiefplugin;

import commands.PigThiefGetCommand;
import events.BondageEvents;
import events.PigThiefEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ThiefPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("ThiefPlugins was enabled");

        new PigThiefEvents(this);
        new BondageEvents(this);


        getCommand("thief").setExecutor(new PigThiefGetCommand());
        getCommand("thief").setTabCompleter(new PigThiefGetCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("ThiefPlugins was disabled");
    }
}
