package craft;

import items.ThiefItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class ThiefCraft {

    public ThiefCraft() {
        recipeLead();
        recipeBondage();
    }

    public void recipeLead() {
        ItemStack pigLead = ThiefItems.getPigLead();
        ShapedRecipe recipePigLead = new ShapedRecipe(pigLead);
        recipePigLead.shape("*%*","%B%","*%*");

        recipePigLead.setIngredient('%', Material.LEAD);
        recipePigLead.setIngredient('*', Material.PORKCHOP);
        recipePigLead.setIngredient('B', Material.SKELETON_SKULL);

        Bukkit.getServer().addRecipe(recipePigLead);
    }

    public void recipeBondage() {
        ItemStack pigBondage = ThiefItems.getBondage();
        ShapedRecipe recipeBondage = new ShapedRecipe(pigBondage);
        recipeBondage.shape("*%*","%B%","*%*");

        recipeBondage.setIngredient('%', Material.IRON_INGOT);
        recipeBondage.setIngredient('*', Material.COBWEB);
        recipeBondage.setIngredient('B', Material.GLOW_INK_SAC);

        Bukkit.getServer().addRecipe(recipeBondage);
    }


}
