package com.isnakebuzz.plugins.task;

import com.isnakebuzz.plugins.Main;
import com.isnakebuzz.plugins.manager.enums.GameState;
import com.isnakebuzz.plugins.utils.Tools;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyTask extends BukkitRunnable {

    private final Main plugin;

    public LobbyTask(Main plugin) {
        this.plugin = plugin;
    }

    private int count = 8;

    @Override
    public void run() {
        plugin.getGameManager().getPlayers().stream().forEach((players) -> {
            players.setLevel(count);
        });
        plugin.getPlayerManager().getBoard().setName("§b§lFightClub §3" + Tools.transform(count));
        if (count == 8) {
            plugin.getGameManager().getPlayers().stream().forEach((players) -> {
                plugin.getMessagesController().sendTitle(players, "§a¡Preparate para luchar!", "&2", 5, 10, 5);
            });
            plugin.getMessagesController().sendBroadcast("&e¡El juego iniciará en §a8§e segundos!.");
        }else if (count > 0 && count <= 5) {
            plugin.getGameManager().getPlayers().stream().map((players) -> {
                plugin.getMessagesController().sendTitle(players, "&c&l" + count, "", 5, 10, 5);
                return players;
            }).map((players) -> {
                players.playSound(players.getLocation(), Sound.BURP, 1F, 1F);
                return players;
            }).map((players) -> {
                players.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 8, 0, true));
                players.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 8, 200, true));
                return players;
            }).forEach((players) -> {
                players.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 8, 0, true));
                players.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 8, 200, true));
            });
            plugin.getMessagesController().sendBroadcast("&eEl juego iniciará en &a" + count + " &esegundos");
        } else if (count == 0) {
            if (plugin.getGameManager().getPlayers().size() == 12) {
                for (Player players : plugin.getGameManager().getPlayers()) {
                    plugin.getArenaManager().getNextSpawnPoint(players);
                    plugin.getPlayerManager().setDefaultKit(players);
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        plugin.getMessagesController().sendTitle(players, "&b&lComenzó!", "&aEs hora de Luchar!", 5, 10, 5);
                    });
                }
                new GameTask(plugin, 1).runTaskTimer(plugin, 20l, 20l);
                GameState.state = GameState.IN_GAME;
                plugin.getSkillManager().checkRegeneracion();
                plugin.getSkillManager().checkVida();
                this.cancel();
            } else {
                count = 4;
                new LobbyTask(plugin).runTaskTimer(plugin, 20l, 20l);
                this.cancel();
            }
        }
        --count;
    }

}
