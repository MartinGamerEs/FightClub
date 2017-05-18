package com.isnakebuzz.plugins.task;

import com.isnakebuzz.plugins.Main;
import com.isnakebuzz.plugins.manager.enums.FaseState;
import com.isnakebuzz.plugins.utils.Tools;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private final Main plugin;
    private final int fase;

    public GameTask(Main plugin, int fase) {
        this.plugin = plugin;
        this.fase = fase;
    }

    private int count;

    @Override
    public void run() {
        if (plugin.getGameManager().isInDeathMatch() || plugin.getGameManager().isEnding()) {
            this.cancel();
        }
        plugin.getPlayerManager().getBoard().setName("§b§lFightClub §6" + Tools.transform(count));
        switch (count) {
            case 0:
                plugin.getMessagesController().sendBroadcast("&7Fase &e&l" + fase);
                switch (fase) {
                    case 1:
                        FaseState.fase = FaseState.FASE_1;
                        break;
                    case 2:
                        FaseState.fase = FaseState.FASE_2;
                        break;
                    case 3:
                        FaseState.fase = FaseState.FASE_3;
                        break;
                }
                plugin.getMessagesController().sendBroadcast("&7La Fase durará 2 minutos, ¡apresúrate!.");
                break;
            case 60:
                plugin.getMessagesController().sendBroadcast("&7Sólo queda &a&l1 MINUTO&7.");
                break;
            case 70:
                plugin.getGameManager().getPlayers().stream().filter((players) -> (!plugin.getGameManager().getPlayersWithKit().contains(players))).map((players) -> {
                    plugin.getGameManager().addPlayerWithKit(players);
                    return players;
                }).map((players) -> {
                    plugin.getPlayerManager().addReward(players);
                    return players;
                }).forEach((players) -> {
                    players.playSound(players.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                });
                break;
            case 115:
                plugin.getArenaManager().reloadSpawns();
                plugin.getArenaManager().setLastSpawnGiven(0);
                plugin.getGameManager().getPlayers().stream().map((players) -> {
                    players.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 1));
                    return players;
                }).forEach((players) -> {
                    players.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 5, 1));
                });
                break;
            case 120:
                plugin.getGameManager().getPlayersWithKit().clear();
                switch (fase) {
                    case 1:
                        if (plugin.getGameManager().isDeathmatchStarted() == true) {
                            plugin.getGameManager().getPlayers().stream().forEach((players) -> {
                                plugin.getArenaManager().teleport(players);
                            });
                            new DeathMatchTask(plugin).runTaskTimer(plugin, 20l, 20l);
                            plugin.getServer().getScheduler().runTask(plugin, () -> {
                                plugin.getGameManager().getPlayers().stream().forEach((p) -> {
                                    p.playSound(p.getLocation(), Sound.ENDERDRAGON_DEATH, 1F, 1F);
                                });
                            });
                        } else if (plugin.getGameManager().isDeathmatchStarted() == false) {
                            plugin.getGameManager().getPlayers().stream().forEach((players) -> {
                                plugin.getArenaManager().getNextSpawnPoint(players);
                            });
                            new GameTask(plugin, 2).runTaskTimer(plugin, 20l, 20l);
                        }
                        break;
                    case 2:
                        if (plugin.getGameManager().isDeathmatchStarted() == true) {
                            plugin.getGameManager().getPlayers().stream().forEach((players) -> {
                                plugin.getArenaManager().teleport(players);
                            });
                            new DeathMatchTask(plugin).runTaskTimer(plugin, 20l, 20l);    
                            plugin.getServer().getScheduler().runTask(plugin, () -> {
                                plugin.getGameManager().getPlayers().stream().forEach((p) -> {
                                    p.playSound(p.getLocation(), Sound.ENDERDRAGON_DEATH, 1F, 1F);
                                });
                            });
                        } else if (plugin.getGameManager().isDeathmatchStarted() == false) {
                            plugin.getGameManager().getPlayers().stream().forEach((players) -> {
                                plugin.getArenaManager().getNextSpawnPoint(players);
                            });
                            new GameTask(plugin, 3).runTaskTimer(plugin, 20l, 20l);
                        }
                        break;
                    case 3:
                        plugin.getGameManager().checkTask();
                        break;
                }
                this.cancel();
                break;
        }

        ++count;

    }

}
