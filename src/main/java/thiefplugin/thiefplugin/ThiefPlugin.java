package thiefplugin.thiefplugin;

import events.Events;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ThiefPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("ThiefPlugins was enabled");

        new Events(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("ThiefPlugins was disabled");
    }
}
