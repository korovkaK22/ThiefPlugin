package events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import thiefplugin.thiefplugin.ThiefPlugin;

public class BondageEvents implements Listener {

    public BondageEvents(ThiefPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //Початок викрадення
    @EventHandler
    private void thiefPlayer(PlayerInteractAtEntityEvent event) {
        if (!(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.NETHERITE_SHOVEL)) {
            return;
        }
        if (!(event.getRightClicked().getType().equals(EntityType.PLAYER))) {
            return;
        }
        //бундаж
        bondagePlayer((Player) event.getRightClicked(), event.getPlayer());
    }


    private void bondagePlayer(Player victim, Player thief) {
        //Убрать поводок з інвентаря
        ItemStack bondage = thief.getInventory().getItemInMainHand();
        bondage.setAmount(bondage.getAmount() - 1);
        thief.getInventory().setItemInMainHand(bondage);

        setEffects(victim,20*60*5);


    }

    //накласти ефекти
    private void setEffects(Player player, int duration) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 5));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 6));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, duration, 250));
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, duration, 0));
    }

}



