package items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ThiefItems {

    public ThiefItems() {
    }

public ItemStack getLead(){
        ItemStack lead = new ItemStack(Material.LEAD,1);
        ItemMeta leadMeta = lead.getItemMeta();
        leadMeta.addEnchant(Enchantment.MENDING,1,false);
        leadMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        leadMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Lead"));

        List<String> lores = new ArrayList<>();
        lores.add(ChatColor.translateAlternateColorCodes('&', "&0&dWith this you can"));
        lores.add(ChatColor.translateAlternateColorCodes('&', "&0&dsteal players :)"));
        leadMeta.setLore(lores);


        lead.setItemMeta(leadMeta);
        return lead;
}



}


