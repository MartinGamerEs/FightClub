package com.isnakebuzz.plugins.listener;

import com.isnakebuzz.plugins.Main;
import com.isnakebuzz.plugins.task.LobbyTask;
import com.isnakebuzz.plugins.utils.ItemLoader;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final Main plugin;

    public PlayerListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (plugin.getGameManager().isInLobby() && plugin.getGameManager().getPlayers().size() < plugin.getArenaManager().getMaxPlayers()) {
            e.allow();
        } else if (e.getPlayer().hasPermission("FightClub.ByPass") && plugin.getGameManager().isInGame() || plugin.getGameManager().isInDeathMatch() || plugin.getGameManager().isEnding()) {
            e.allow();
        } else {
            e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "Jugando");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(null);
        if (plugin.getGameManager().isInLobby() && plugin.getGameManager().getPlayers().size() < plugin.getArenaManager().getMaxPlayers()) {
            plugin.getGameManager().addPlayerToGame(player);
            plugin.getPlayerManager().setLobbyPlayer(player);
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                plugin.getMessagesController().sendBroadcast("&7Ha entrado al juego &e" + player.getDisplayName() + " &6" + plugin.getGameManager().getPlayers().size() + "&2/&6" + plugin.getArenaManager().getMaxPlayers());
                player.getInventory().addItem(ItemLoader.getSelector());
                player.getInventory().addItem(ItemLoader.getGameModificator());
                player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1f, 1f);
            });
            if (plugin.getGameManager().isNotStarted() && plugin.getGameManager().getPlayers().size() == plugin.getArenaManager().getMinPlayers()) {
                plugin.getGameManager().setStarted(true);
                new LobbyTask(plugin).runTaskTimer(plugin, 20l, 20l);
            }
        } else {
            plugin.getPlayerManager().setSpectator(player);
            plugin.getPlayerManager().setupScoreboard(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        if (plugin.getGameManager().isInLobby()) {
            plugin.getGameManager().removePlayerFromGame(e.getPlayer());
            plugin.getMessagesController().sendBroadcast("&7Abandonó el juego &e" + e.getPlayer().getDisplayName() + " &6" + plugin.getGameManager().getPlayers().size() + "&2/&6" + plugin.getArenaManager().getMaxPlayers());
        } else if (plugin.getGameManager().isInGame() || plugin.getGameManager().isInDeathMatch()) {
            plugin.getGameManager().removePlayerFromGame(e.getPlayer());
            plugin.getGameManager().removeSpectator(e.getPlayer());
            plugin.getGameManager().removePlayerWithKit(e.getPlayer());
            if (plugin.getGameManager().getPlayers().contains(e.getPlayer())) {
                plugin.getGameManager().checkWin();
                plugin.getGameManager().checkDM();
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    plugin.getMessagesController().sendBroadcast("&e" + e.getPlayer().getDisplayName() + " &7ha muerto desconectado.");
                });
            }
        } else {
            plugin.getGameManager().removePlayerFromGame(e.getPlayer());
            plugin.getGameManager().removeSpectator(e.getPlayer());
            plugin.getGameManager().removePlayerWithKit(e.getPlayer());
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerKickEvent e) {
        e.setLeaveMessage(null);
        if (plugin.getGameManager().isInLobby()) {
            plugin.getGameManager().removePlayerFromGame(e.getPlayer());
            plugin.getMessagesController().sendBroadcast("&7Abandonó el juego &e" + e.getPlayer().getDisplayName() + " &6" + plugin.getGameManager().getPlayers().size() + "&2/&6" + plugin.getArenaManager().getMaxPlayers());
        } else if (plugin.getGameManager().isInGame() || plugin.getGameManager().isInDeathMatch()) {
            plugin.getGameManager().removePlayerFromGame(e.getPlayer());
            plugin.getGameManager().removeSpectator(e.getPlayer());
            plugin.getGameManager().removePlayerWithKit(e.getPlayer());
            if (plugin.getGameManager().getPlayers().contains(e.getPlayer())) {
                plugin.getGameManager().checkWin();
                plugin.getGameManager().checkDM();
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    plugin.getMessagesController().sendBroadcast("&e" + e.getPlayer().getDisplayName() + " &7ha muerto desconectado.");
                });
            }
        } else {
            plugin.getGameManager().removePlayerFromGame(e.getPlayer());
            plugin.getGameManager().removeSpectator(e.getPlayer());
            plugin.getGameManager().removePlayerWithKit(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        if (plugin.getGameManager().isInGame() || plugin.getGameManager().isInDeathMatch()) {
            if (e.getEntity().getKiller() instanceof Player) {
                plugin.getGameManager().removePlayerFromGame(e.getEntity());
                plugin.getPlayerManager().setSpectator(e.getEntity());
                plugin.getGameManager().removePlayerWithKit(e.getEntity());
                plugin.getPlayerManager().addReward(e.getEntity().getKiller());
                plugin.getGameManager().addPlayerWithKit(e.getEntity().getKiller());
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    plugin.getSkillManager().setReward(e);
                    plugin.getMessagesController().sendBroadcast("&e" + e.getEntity().getDisplayName() + " &7ha muerto por &e" + e.getEntity().getKiller().getDisplayName() + "&7.");
                });
            } else {
                plugin.getGameManager().removePlayerFromGame(e.getEntity());
                plugin.getPlayerManager().setSpectator(e.getEntity());
                plugin.getGameManager().removePlayerWithKit(e.getEntity());
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    plugin.getMessagesController().sendBroadcast("&e" + e.getEntity().getDisplayName() + " &7ha muerto.");
                });
            }
            plugin.getGameManager().checkWin();
            plugin.getGameManager().checkDM();
        }
    }

    public void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void unregisterAllPlayerEvents() {
        HandlerList.unregisterAll(this);
    }

}
