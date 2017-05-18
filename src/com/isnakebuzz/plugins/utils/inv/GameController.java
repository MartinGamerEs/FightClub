package com.isnakebuzz.plugins.utils.inv;

import com.isnakebuzz.plugins.utils.ItemLoader;
import com.isnakebuzz.plugins.Main;
import org.bukkit.plugin.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import java.util.*;

public class GameController implements Listener
{
    private final Main plugin;
    
    public GameController(final Main plugin) {
        this.plugin = plugin;
    }
    
    public void init() {
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(final PlayerInteractEvent e) {
        if (this.plugin.getGameManager().isInLobby() && e.getPlayer().getInventory().getItemInHand() != null) {
            if (e.getPlayer().getInventory().getItemInHand().equals((Object)ItemLoader.getSelector())) {
                if (e.getPlayer().hasPermission("FightClub.VIP")) {
                    this.loadMenu(e.getPlayer());
                }
                else {
                    this.plugin.getMessagesController().sendMessage(e.getPlayer(), "&e¡Obt\u00e9n &3&lDorito &eo superior en &6http://tienda.doricrafters.net/ &e!");
                }
            }
            else if (e.getPlayer().getInventory().getItemInHand().equals((Object)ItemLoader.getGameModificator())) {
                if (e.getPlayer().hasPermission("FightClub.VIP")) {
                    this.loadGameModifactorMenu(e.getPlayer());
                }
                else {
                    this.plugin.getMessagesController().sendMessage(e.getPlayer(), "&e¡Obt\u00e9n &3&lDorito &eo superior en &6http://tienda.doricrafters.net/ &e!");
                }
            }
        }
    }
    
    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        if (this.plugin.getGameManager().isInLobby() && event.getCurrentItem() != null && event.getCurrentItem().getType() != null) {
            event.setCancelled(true);
            if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
                event.setCancelled(true);
                final ItemStack clicked = event.getCurrentItem();
                if (clicked != null) {
                    if (event.getInventory().getTitle().equalsIgnoreCase("§3Selector de clase")) {
                        if (clicked.getItemMeta().getDisplayName().equals("§aClase de experiencia")) {
                            if (player.hasPermission("FightClub.Experencia")) {
                                this.plugin.getSkillManager().addPlayerToEnchanterSkill(player);
                                player.closeInventory();
                                this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, () -> this.plugin.getMessagesController().sendActionBar(player, "&d&lSELECCIONASTE LA CLASE: &a&lEXPERIENCIA"));
                            }
                            else {
                                this.plugin.getMessagesController().sendMessage(player, "&e¡Obt\u00e9n &5&lVIP &eo superior en &6http://tienda.doricrafters.net/ &e!");
                            }
                        }
                        if (clicked.getItemMeta().getDisplayName().equals("§dClase mago")) {
                            if (player.hasPermission("FightClub.Mago")) {
                                this.plugin.getSkillManager().addPlayerToWizardSkill(player);
                                player.closeInventory();
                                this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, () -> this.plugin.getMessagesController().sendActionBar(player, "&d&lSELECCIONASTE LA CLASE: &a&lMAGO"));
                            }
                            else {
                                this.plugin.getMessagesController().sendMessage(player, "&e¡Obt\u00e9n &5&lVIP &eo superior en &6http://tienda.doricrafters.net/ &e!");
                            }
                        }
                    }
                    else if (event.getInventory().getTitle().equalsIgnoreCase("§3Modificador de Juego")) {
                        if (player.hasPermission("FightClub.Dorito")) {
                         if (clicked.getItemMeta().getDisplayName().equals("§cModificador de vida")) {
                            this.regenerationMenu(player);
                        }
                        }
                        if (player.hasPermission("FightClub.Dorito+")) {
                        if (clicked.getItemMeta().getDisplayName().equals("§aNiveles de vida")) {
                            this.nivelVidaMenu(player);
                        }   
                        }
                    }
                    else if (event.getInventory().getTitle().equalsIgnoreCase("§lRegeneraci\u00f3n de vida")) {
                        if (clicked.getItemMeta().getDisplayName().equals("§dRegeneraci\u00f3n VANILLA")) {
                            this.plugin.getSkillManager().addVoteToNormal(player);
                            player.closeInventory();
                        }
                        if (clicked.getItemMeta().getDisplayName().equals("§6Regeneraci\u00f3n UHC")) {
                            this.plugin.getSkillManager().addVoteToUHC(player);
                            player.closeInventory();
                        }
                    }
                    else if (event.getInventory().getTitle().equalsIgnoreCase("§3Niveles de vida")) {
                        if (clicked.getItemMeta().getDisplayName().equals("§aVida Normal")) {
                            this.plugin.getSkillManager().addVoteNormalVida(player);
                            player.closeInventory();
                        }
                        if (clicked.getItemMeta().getDisplayName().equals("§aDoble Vida")) {
                            this.plugin.getSkillManager().addVoteDobleVida(player);
                            player.closeInventory();
                        }
                        if (clicked.getItemMeta().getDisplayName().equals("§aTriple Vida")) {
                            this.plugin.getSkillManager().addVotrTripleVida(player);
                            player.closeInventory();
                        }
                    }
                }
            }
        }
    }
    
    public void loadMenu(final Player player) {
        final Inventory inv = this.plugin.getServer().createInventory((InventoryHolder)null, 9, "§3Selector de clase");
        final ItemStack XP = new ItemStack(Material.EXP_BOTTLE);
        final ItemMeta meta = XP.getItemMeta();
        meta.setDisplayName("§aClase de experiencia");
        XP.setItemMeta(meta);
        inv.setItem(0, XP);
        final ItemStack EN = new ItemStack(Material.ENCHANTED_BOOK);
        final ItemMeta meta2 = EN.getItemMeta();
        meta2.setDisplayName("§dClase mago");
        EN.setItemMeta(meta2);
        inv.setItem(1, EN);
        player.openInventory(inv);
    }
    
    public void loadGameModifactorMenu(final Player player) {
        final Inventory Invm = this.plugin.getServer().createInventory((InventoryHolder)null, 27, "§3Modificador de Juego");
        final ItemStack lifeCount = new ItemStack(Material.REDSTONE);
        final ItemMeta lfm = lifeCount.getItemMeta();
        lfm.setDisplayName("§aNiveles de vida");
        lfm.setLore((List)Arrays.asList("§7Elige los niveles de vida para la partida"));
        lifeCount.setItemMeta(lfm);
        Invm.setItem(12, lifeCount);
        final ItemStack health = new ItemStack(Material.EMERALD);
        final ItemMeta hm = health.getItemMeta();
        hm.setDisplayName("§cModificador de vida");
        hm.setLore((List)Arrays.asList("§7Vota por la regeneraci\u00f3n de vida normal o UHC"));
        health.setItemMeta(hm);
        Invm.setItem(13, health);
        final ItemStack booster = new ItemStack(Material.POTION);
        final ItemMeta bm = booster.getItemMeta();
        bm.setDisplayName("§6Multiplicadores de \u00cdtems");
        bm.setLore((List)Arrays.asList("§7Obt\u00e9n el doble de \u00edtems al matar!"));
        booster.setItemMeta(bm);
        Invm.setItem(14, booster);
        player.openInventory(Invm);
    }
    
    public void nivelVidaMenu(final Player player) {
        final Inventory vida = this.plugin.getServer().createInventory((InventoryHolder)null, 9, "§3Niveles de vida");
        final ItemStack level1 = new ItemStack(Material.DOUBLE_PLANT);
        final ItemMeta lm = level1.getItemMeta();
        lm.setDisplayName("§aVida Normal");
        lm.setLore((List)Arrays.asList("§7La barra normal de vida", "§7Votos: §e" + this.plugin.getSkillManager().getNormalVida().size()));
        level1.setItemMeta(lm);
        vida.setItem(3, level1);
        final ItemStack level2 = new ItemStack(Material.DOUBLE_PLANT, 2);
        final ItemMeta lm2 = level2.getItemMeta();
        lm2.setDisplayName("§aDoble Vida");
        lm2.setLore((List)Arrays.asList("§7La doble barra de vida", "§7Votos: §e" + this.plugin.getSkillManager().getDobleVida().size()));
        level2.setItemMeta(lm2);
        vida.setItem(4, level2);
        final ItemStack level3 = new ItemStack(Material.DOUBLE_PLANT, 3);
        final ItemMeta lm3 = level3.getItemMeta();
        lm3.setDisplayName("§aTriple Vida");
        lm3.setLore((List)Arrays.asList("§7La triple barra de vida", "§7Votos: §e" + this.plugin.getSkillManager().getTriplevida().size()));
        level3.setItemMeta(lm3);
        vida.setItem(5, level3);
        player.openInventory(vida);
    }
    
    public void regenerationMenu(final Player player) {
        final Inventory rm = this.plugin.getServer().createInventory((InventoryHolder)null, 9, "§lRegeneraci\u00f3n de vida");
        final ItemStack NORMAL = new ItemStack(Material.APPLE);
        final ItemMeta nm = NORMAL.getItemMeta();
        nm.setDisplayName("§dRegeneraci\u00f3n VANILLA");
        nm.setLore((List)Arrays.asList("§7Vota por regeneraci\u00f3n VANILLA", "§7Votos: §e" + this.plugin.getSkillManager().getNaturalRegeneration().size()));
        NORMAL.setItemMeta(nm);
        rm.setItem(3, NORMAL);
        final ItemStack UHC = new ItemStack(Material.GOLDEN_APPLE);
        final ItemMeta um = UHC.getItemMeta();
        um.setDisplayName("§6Regeneraci\u00f3n UHC");
        um.setLore((List)Arrays.asList("§7Vota por regeneraci\u00f3n UHC", "§7Votos: §e" + this.plugin.getSkillManager().getUhcRegeneration().size()));
        UHC.setItemMeta(um);
        rm.setItem(5, UHC);
        player.openInventory(rm);
    }
}
