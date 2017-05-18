package com.isnakebuzz.plugins.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemLoader {

    private static ItemStack ironHelmet;
    private static ItemStack ironChestplate;
    private static ItemStack ironLeggins;
    private static ItemStack ironBoots;

    private static ItemStack sword;
    private static ItemStack food;

    private static ItemStack xp;
    private static ItemStack lapiz;
    private static ItemStack ep1;
    private static ItemStack ep2;
    private static ItemStack es1;
    private static ItemStack pr1;
    private static ItemStack pd1;
    private static ItemStack ebp1;
    private static ItemStack bow;
    private static ItemStack arrow;

    private static ItemStack xp2;
    private static ItemStack goldenApple;
    private static ItemStack p2;
    private static ItemStack s2;
    private static ItemStack ih2;
    private static ItemStack lp2;
    private static ItemStack arrow2;
    private static ItemStack rod;
    
    private static ItemStack bd3;
    private static ItemStack s3;
    
    private static ItemStack selector;
    private static ItemStack gameModificator;
    
    public static void init() {
        //Started kit
        ironHelmet = new ItemStack(Material.IRON_HELMET);
        ironChestplate = new ItemStack(Material.IRON_CHESTPLATE);
        ironLeggins = new ItemStack(Material.IRON_LEGGINGS);
        ironBoots = new ItemStack(Material.IRON_BOOTS);
        sword = new ItemStack(Material.IRON_SWORD);
        food = new ItemStack(Material.COOKED_BEEF, 16);
        //Fase1
        xp = new ItemStack(Material.EXP_BOTTLE, 16);
        lapiz = new ItemStack(Material.INK_SACK, 8, (byte) 4);
        ep1 = getCustomEnchantedBook(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ep2 = getCustomEnchantedBook(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        es1 = getCustomEnchantedBook(Enchantment.DAMAGE_ALL, 1);
        pr1 = new ItemStack(Material.POTION, 1, (short) 16385);
        pd1 = new ItemStack(Material.POTION, 1, (short) 16460);
        bow = new ItemStack(Material.BOW);
        ebp1 = getCustomEnchantedBook(Enchantment.ARROW_DAMAGE, 1);
        arrow = new ItemStack(Material.ARROW, 8);
        //Fase2
        xp2 = new ItemStack(Material.EXP_BOTTLE, 8);
        goldenApple = new ItemStack(Material.GOLDEN_APPLE);
        p2 = getCustomEnchantedBook(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        s2 = getCustomEnchantedBook(Enchantment.DAMAGE_ALL, 1);
        ih2 = new ItemStack(Material.POTION, 1, (short) 16421);
        lp2 = new ItemStack(Material.INK_SACK, 4, (byte) 4);
        arrow2 = new ItemStack(Material.ARROW, 4);
        rod = new ItemStack(Material.FISHING_ROD, 1);
        //Fase 3
        bd3 = getCustomEnchantedBook(Enchantment.ARROW_DAMAGE, 2);
        s3 = getCustomEnchantedBook(Enchantment.DAMAGE_ALL, 2);
        selector = new ItemStack(Material.BOOK);
        ItemMeta meta = selector.getItemMeta();
        meta.setDisplayName("§3Selector de Clases §7(Click Derecho)");
        selector.setItemMeta(meta);
        gameModificator = new ItemStack(Material.CHEST);
        ItemMeta gmm = gameModificator.getItemMeta();
        gmm.setDisplayName("§3Modificador de Juego §7(Click Derecho)");
        gameModificator.setItemMeta(gmm);
    }

    public static ItemStack getSelector() {
        return selector;
    }

    public static ItemStack getGameModificator() {
        return gameModificator;
    }

    
    
    
    public static ItemStack getS3() {
        return s3;
    }
    
    
    
    public static ItemStack getBd3() {
        return bd3;
    }

    
    
    public static ItemStack getXp2() {
        return xp2;
    }

    public static ItemStack getGoldenApple() {
        return goldenApple;
    }

    public static ItemStack getP2() {
        return p2;
    }

    public static ItemStack getS2() {
        return s2;
    }

    public static ItemStack getIh2() {
        return ih2;
    }

    public static ItemStack getLp2() {
        return lp2;
    }

    public static ItemStack getArrow2() {
        return arrow2;
    }

    public static ItemStack getIronBoots() {
        return ironBoots;
    }

    public static ItemStack getIronChestplate() {
        return ironChestplate;
    }

    public static ItemStack getIronHelmet() {
        return ironHelmet;
    }

    public static ItemStack getIronLeggins() {
        return ironLeggins;
    }

    public static ItemStack getSword() {
        return sword;
    }

    public static ItemStack getFood() {
        return food;
    }

    public static ItemStack getXp() {
        return xp;
    }

    public static ItemStack getLapiz() {
        return lapiz;
    }

    public static ItemStack getEp1() {
        return ep1;
    }

    public static ItemStack getEp2() {
        return ep2;
    }

    public static ItemStack getEs1() {
        return es1;
    }

    public static ItemStack getPd1() {
        return pd1;
    }

    public static ItemStack getPr1() {
        return pr1;
    }

    public static ItemStack getEbp1() {
        return ebp1;
    }

    public static ItemStack getArrow() {
        return arrow;
    }

    public static ItemStack getRod() {
        return rod;
    }
    
    public static ItemStack getBow() {
        return bow;
    }

    private static ItemStack getCustomEnchantedBook(Enchantment enchant, int level) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(enchant, level, true);
        item.setItemMeta(meta);
        return item;

    }

}
