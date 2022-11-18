package events;


import org.bukkit.Bukkit;
import org.bukkit.entity.Evoker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import thiefplugin.thiefplugin.ThiefPlugin;

public class Events implements Listener {
    ThiefPlugin plugin;


    public Events(ThiefPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }


    //Посадить на хрюшу
    @EventHandler
    public void thiefPlayer(PlayerPickupArrowEvent event) {


    }


}