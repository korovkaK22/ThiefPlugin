package events;

import items.ThiefItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import thiefplugin.thiefplugin.ThiefPlugin;

public class BondageEvents implements Listener {
    private final int duration= 20*60*2;
    private final String toVictim= "Oh no, you were captured! Time remain: ";
    private final String toThief= "That's it! Time remain: ";

    public BondageEvents(ThiefPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //Початок викрадення
    @EventHandler
    private void thiefPlayer(PlayerInteractAtEntityEvent event) {
        if (!(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.STRING)) {
            return;
        }
        if (!(event.getRightClicked().getType().equals(EntityType.PLAYER))) {
            return;
        }
        //бундаж

        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta()
                .equals(ThiefItems.getBondage().getItemMeta())) {
            bondagePlayer((Player) event.getRightClicked(), event.getPlayer());
        }
    }

    @EventHandler
    private void setblockString(BlockPlaceEvent event) {
        if (event.getItemInHand().getType()!=Material.STRING){
            return;
        }
       if (event.getItemInHand().getItemMeta().equals(ThiefItems.getBondage().getItemMeta())) {
           Player player =  event.getPlayer();
           player.sendMessage(ChatColor.RED + "Nope");
           player.playSound(player, Sound.ENTITY_VILLAGER_NO, 0.9f, 1);//Звук реджекта
           event.setCancelled(true);
       }
    }




    private void bondagePlayer(Player victim, Player thief) {
        //Убрать поводок з інвентаря
        ItemStack bondage = thief.getInventory().getItemInMainHand();
        bondage.setAmount(bondage.getAmount() - 1);
        thief.getInventory().setItemInMainHand(bondage);

        //Дать ефекти
        setEffects(victim);

        int time =victim.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration()/20/60;

        victim.sendMessage(ChatColor.RED +toVictim+time+ " min!");
        victim.playSound( victim, Sound.ENTITY_VILLAGER_NO, 0.9f, 1); //Звук реджекта

        thief.sendMessage(ChatColor.GREEN +toThief+time+ " min!");
        thief.playSound(thief, Sound.ENTITY_PLAYER_LEVELUP, 0.9f, 1); //звук ассепта


    }

    //накласти ефекти
    private void setEffects(Player player) {
        int damage_res =0;
        int slow =0;
        int slow_digging =0;
        int jump =0;
        int glowing =0;

        if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
            damage_res= player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration();}

        if (player.hasPotionEffect(PotionEffectType.SLOW)){
            slow= player.getPotionEffect(PotionEffectType.SLOW).getDuration();}

        if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)){
            slow_digging= player.getPotionEffect(PotionEffectType.SLOW_DIGGING).getDuration();}

        if (player.hasPotionEffect(PotionEffectType.JUMP)){
            jump= player.getPotionEffect(PotionEffectType.JUMP).getDuration();}

        if (player.hasPotionEffect(PotionEffectType.GLOWING)){
            glowing= player.getPotionEffect(PotionEffectType.GLOWING).getDuration();}


        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,damage_res+ duration, 5));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,slow+ duration, 6));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,slow_digging+ duration, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,jump+ duration, 250));
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,glowing+ duration, 0));
    }

}



