package events;


import items.ThiefItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import thiefplugin.thiefplugin.ThiefPlugin;

import java.util.HashMap;
import java.util.Map;


public class PigThiefEvents implements Listener {
    private final String noEscape = "Sit here!";
    private final String newVictim = "Oh no, you were stolen!";
    private final String successThief = "You have successful steal";
    Map<String, Pig> pigsByPlayers;

    public PigThiefEvents(ThiefPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
      //  this.sittingPlayers = new LinkedList<>();
        this.pigsByPlayers= new HashMap<>();
    }


    //Початок викрадення
    @EventHandler
    private void thiefPlayer(PlayerInteractAtEntityEvent event) {
        if (pigsByPlayers.containsKey(event.getPlayer().getName())) {
            //отмена евенту
            event.getPlayer().sendMessage(ChatColor.RED + noEscape);
            event.setCancelled(true);
             return;
        }

        if (!(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEAD)) {
            return;
        }
        if (!(event.getRightClicked().getType().equals(EntityType.PLAYER))) {
            return;
        }

        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta()
                .equals(ThiefItems.getPigLead().getItemMeta())) {
            //садиться
            makePig((Player) event.getRightClicked(), event.getPlayer());
        }
    }


    //Посадить на хрюшу
    private void makePig(Player victim, Player thief) {

        //Убрать поводок з інвентаря
        ItemStack lead = thief.getInventory().getItemInMainHand();
        lead.setAmount(lead.getAmount() - 1);
        thief.getInventory().setItemInMainHand(lead);

        //Посадить на хрюшку
        Pig pig = (Pig) victim.getWorld().spawnEntity(victim.getLocation(), EntityType.PIG);
        pig.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 50000, 0));
        pig.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 50000, 0));
        pig.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 50000, 0));

        pig.addPassenger(victim);
        pig.setLeashHolder(thief);

        pigsByPlayers.put(victim.getName(),pig);

        thief.sendMessage(ChatColor.GREEN + successThief + " " + victim.getName() + " !");
        thief.playSound(thief, Sound.ENTITY_PLAYER_LEVELUP, 0.9f, 1); //звук ассепта

        victim.sendMessage(ChatColor.RED + newVictim);
        victim.playSound(victim, Sound.ENTITY_VILLAGER_NO, 0.9f, 1);

        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + thief.getName() + " steal " + victim.getName() + " !");


        setEffects(victim, 20 * 60 * 10);
    }


    //Вихід гравця
    @EventHandler
    private void quitPlayer(PlayerQuitEvent event) {

    }


    //Пробує злізти
    @EventHandler
    private void reExitPlayer(VehicleExitEvent event) {
        if (!(event.getExited() instanceof Player player)) {
            return;
        }

        if (pigsByPlayers.containsKey(player.getName()) && event.getVehicle() instanceof Pig) {
            //отмена евенту
            player.sendMessage(ChatColor.RED + noEscape);
            event.setCancelled(true);
        }

    }

    @EventHandler
    private void joinWorld(PlayerJoinEvent event) {

        if(pigsByPlayers.containsKey(event.getPlayer().getName())){
            Player player = event.getPlayer();
            Pig pig =pigsByPlayers.get(player.getName());
            if(event.getPlayer().getWorld().equals(pig.getWorld())){
                pig.addPassenger(player);
            }
        }
    }

    @EventHandler
    private void changeWorld(PlayerChangedWorldEvent event) {
        if(pigsByPlayers.containsKey(event.getPlayer().getName())){
            Player player = event.getPlayer();
            Pig pig =pigsByPlayers.get(player.getName());
          if(event.getPlayer().getWorld().equals(pig.getWorld())){
              pig.addPassenger(player);
            }
        }
    }


    //Пробує пересісти
    @EventHandler
    private void reSitPlayer(VehicleEnterEvent event) {
        if (!(event.getEntered() instanceof Player player)) {
            return;
        }
        //Для перезаходу гравців
        if (event.getVehicle() instanceof Pig pig) {
            if (!pig.hasSaddle()) {
                return;
            }
        }

        if (pigsByPlayers.containsKey(player.getName())) {
            //отмена евенту
            player.sendMessage(ChatColor.RED + noEscape);
            event.setCancelled(true);
            }
    }


    //Вилізти через пьорл або хорус
    @EventHandler
    private void teleportPlayer(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (pigsByPlayers.containsKey(player.getName())) {
            player.sendMessage(ChatColor.RED + noEscape);
            event.setCancelled(true);
        }
    }


    //накласти ефекти
    private void setEffects(Player player, int duration) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 5));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, duration, 0));
    }


    //Якщо хрюшу вбивають, то чиститься все сразу
    @EventHandler
    private void pigDie(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Pig)) {
            return;
        }
        Pig pig = (Pig) event.getEntity();

        if (pig.hasSaddle() || pig.getPassengers().isEmpty()) {
            return;
        }

        for (Entity passenger : pig.getPassengers()) {
            if (passenger instanceof Player) {
                if (pigsByPlayers.containsKey(passenger.getName())) {
                    pigsByPlayers.remove(passenger.getName());
                    removeEffects((Player) passenger);
                }
            }
        }


    }

    private void removeEffects(Player player) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        player.removePotionEffect(PotionEffectType.GLOWING);
    }


    //Гравець вже не сидить на хрюшці
//    private void removeSheduleEffect(Player player) {
//       Integer taskId= threadSitters.get(player);
//       threadSitters.remove(player);
//
//       Bukkit.getScheduler().cancelTask(taskId);
//
//    }

//    //Зробить провірку, чи сидить на хрюшці
//    private void makeSheduleEffect(Player player) {
//        Runnable thread = new Runnable() {
//            @Override
//            public void run() {
//                if (player.getVehicle() instanceof Pig && !(((Pig) player.getVehicle()).hasSaddle())) {
//                } else {
//                    removeSheduleEffect(player);
//                }
//            }
//        };
//
//     Integer taskId=  Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, thread
//    ,0L,200L);
//     threadSitters.put(player,taskId);
//}


}