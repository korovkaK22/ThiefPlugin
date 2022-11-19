package events;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
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
import java.util.LinkedList;
import java.util.Map;


public class Events implements Listener {
    private final String noEscape = "Sit here!";
    private final String newVictim = "Oh no, you were stolen!";
    private final String successThief = "You have successful steal";
    private final ThiefPlugin plugin;
    LinkedList<String> sittingPlayers;
    Map<String, Pig> pigsByPlayers;

    public Events(ThiefPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        this.sittingPlayers = new LinkedList<>();
        this.pigsByPlayers= new HashMap<>();
    }


    //Початок викрадення
    @EventHandler
    private void thiefPlayer(PlayerInteractAtEntityEvent event) {

        if (sittingPlayers.contains(event.getPlayer().getName())) {
            //отмена евенту
            Bukkit.broadcastMessage("interuct reset");//=========
            event.getPlayer().sendMessage(ChatColor.RED + noEscape);
            event.setCancelled(true);
 Bukkit.broadcastMessage( event.getPlayer().getVehicle()==null? "null":  //===========
         event.getPlayer().getVehicle().toString());//================
            return;
        }

        if (!(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.NETHERITE_SWORD)) {
            return;
        }
        if (!(event.getRightClicked().getType().equals(EntityType.PLAYER))) {
            return;
        }
        //садиться
        makePig((Player) event.getRightClicked(), event.getPlayer());
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
        victim.sendMessage(ChatColor.RED + newVictim);
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + thief.getName() + " steal " + victim.getName() + " !");


        setEffects(victim, 20 * 60 * 10);
    }


    //Вихід гравця
    @EventHandler
    private void quitPlayer(PlayerQuitEvent event) {
        sittingPlayers.remove(event.getPlayer().getName());
        Player player =event.getPlayer();
        pigsByPlayers.get(player.getName()).addPassenger(player);
    }


    //Пробує злізти
    @EventHandler
    private void reExitPlayer(VehicleExitEvent event) {
        if (!(event.getExited() instanceof Player player)) {
            return;
        }

        if (sittingPlayers.contains(player.getName()) && event.getVehicle() instanceof Pig) {
            //отмена евенту
            player.sendMessage(ChatColor.RED + noEscape);
            event.setCancelled(true);
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
                if (!sittingPlayers.contains(player.getName())) {
                    sittingPlayers.add(player.getName());
                    pigsByPlayers.remove(player.getName());
                    pigsByPlayers.put(player.getName(),(Pig) event.getVehicle());
                }
                return;
            }
        }

        if (sittingPlayers.contains(player.getName())) {
            //отмена евенту
            player.sendMessage(ChatColor.RED + noEscape);
            event.setCancelled(true);
            }
    }


    //Вилізти через пьорл або хорус
    @EventHandler
    private void teleportPlayer(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (sittingPlayers.contains(player.getName())) {
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
                if (sittingPlayers.contains(passenger.getName())) {
                    sittingPlayers.remove(passenger.getName());
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