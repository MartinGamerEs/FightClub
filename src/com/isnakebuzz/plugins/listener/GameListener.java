package com.isnakebuzz.plugins.listener;

import com.isnakebuzz.plugins.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class GameListener implements Listener {

    private final Main plugin;

    public GameListener(Main plugin) {
        this.plugin = plugin;
    }

    private BukkitTask fallControl;
    
    @EventHandler
    public void onMotdChange(ServerListPingEvent e){
        if(plugin.getGameManager().isInLobby()){
            e.setMotd("Esperando");
        } else if(plugin.getGameManager().isInGame()){
            e.setMotd("Jugando");
        } else if(plugin.getGameManager().isInDeathMatch()){
            e.setMotd("Jugando");
        } else if(plugin.getGameManager().isEnding()){
            e.setMotd("Reiniciando");
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (plugin.getGameManager().isInGame() || plugin.getGameManager().isInDeathMatch()) {
            e.setCancelled(false);
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent e) {
        if (plugin.getGameManager().isInGame() || plugin.getGameManager().isInDeathMatch()) {
            e.setCancelled(false);
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (plugin.getGameManager().isInGame() || plugin.getGameManager().isInDeathMatch()) {
            e.setCancelled(false);
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        org.bukkit.block.Block block = e.getBlock();
        Player player = e.getPlayer();

        
        //Wood//
        ItemStack cow = new ItemStack(Material.STRING);
        cow.setAmount(1);
        
        
        
        //Wood//
        if (block.getType() == Material.WEB){
        e.setCancelled(true);
        player.getInventory().addItem(cow);
       }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    public void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        if (fallControl == null) {
            fallControl = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                plugin.getServer().getOnlinePlayers().stream().filter((players) -> (plugin.getGameManager().getSpectators().contains(players))).filter((players) -> (players.getLocation().getBlockY() <= 0)).forEach((players) -> {
                    players.teleport(plugin.getArenaManager().getLobby());
                });
            }, 20, 20);
        }
    }

    public void unregisterAllGameEvents() {
        HandlerList.unregisterAll(this);
        fallControl.cancel();
    }

}
