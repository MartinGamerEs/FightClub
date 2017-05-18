package com.isnakebuzz.plugins.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.isnakebuzz.plugins.Main;
import com.isnakebuzz.plugins.utils.Tools;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ArenaManager {

    private final Main plugin;

    public ArenaManager(Main plugin) {
        this.plugin = plugin;
        spawnPoints = new ArrayList<>();
        dmSpawnPoints = new ArrayList<>();
        this.rand = new Random();
    }

    private int lastSpawnGiven;
    private int spawn;
    private int spawnDM;

    private final Random rand;
    private final List<Location> spawnPoints;
    private final List<Location> dmSpawnPoints;

    private int maxPlayers;
    private int minPlayers;

    private String serverID;

    private Location lobby;

    public void init() {
        lobby = Tools.stringToLoc(plugin.getArenaConfiguration().getArenaConfig().getString("lobby"));
        minPlayers = plugin.getArenaConfiguration().getArenaConfig().getInt("min");
        maxPlayers = plugin.getArenaConfiguration().getArenaConfig().getInt("max");
        serverID = plugin.getConfig().getString("serverID");
        loadSpawns();
        prepareWorld(plugin.getArenaWorld());
    }

    public void reload() {
        loadSpawns();
        removeItems();
        plugin.getGameManager().init();
        plugin.getSkillManager().init();
        for (Player players : plugin.getServer().getOnlinePlayers()) {
            players.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
            players.teleport(lobby);
            plugin.getPlayerManager().setLobbyPlayer(players);
            plugin.getGameManager().addPlayerToGame(players);
            
        }
    }

    private void prepareWorld(World world) {
        world.setPVP(true);
        world.setDifficulty(Difficulty.NORMAL);
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setStorm(false);
        world.setTime(6000l);
    }

    private void loadSpawns() {
        spawnPoints.clear();
            dmSpawnPoints.clear();
        try {
            for (int i = 1; i <= maxPlayers; i++) {
                spawnPoints.add(Tools.stringToLoc(plugin.getArenaConfiguration().getArenaConfig().getString("spawnPoints." + i)));
            }
            for (int i = 1; i <= 2; i++) {
                dmSpawnPoints.add(Tools.stringToLoc(plugin.getArenaConfiguration().getArenaConfig().getString("dmSpawnPoints." + i)));
            }
        } catch (Exception e) {
        }
    }

    public void reloadSpawns() {
        spawnPoints.clear();
        try {
            for (int i = 1; i <= maxPlayers; i++) {
                spawnPoints.add(Tools.stringToLoc(plugin.getArenaConfiguration().getArenaConfig().getString("spawnPoints." + i)));
            }
        } catch (Exception e) {

        }
    }

    public void teleport(Player player) {
        int ranint = rand.nextInt(dmSpawnPoints.size());
        player.teleport(dmSpawnPoints.get(ranint));
        dmSpawnPoints.remove(ranint);
    }

    public void setSpawnLocation(Player player) {
        String l = Tools.locationToString(player.getLocation());
        spawn = spawn + 1;
        plugin.getArenaConfiguration().getArenaConfig().set("spawnPoints." + spawn, l);
        plugin.getArenaConfiguration().save();
        spawnPoints.clear();
        for (int i = 1; i <= spawn; i++) {
            spawnPoints.add(Tools.stringToLoc(plugin.getArenaConfiguration().getArenaConfig().getString("spawnPoints." + i)));
        }
        plugin.getMessagesController().sendMessage(player, "&7Has puesto el spawn &e&l" + spawn);
    }

    public void setDMSpawnLocation(Player player) {
        String l = Tools.locationToString(player.getLocation());
        spawnDM = spawnDM + 1;
        plugin.getArenaConfiguration().getArenaConfig().set("dmSpawnPoints." + spawnDM, l);
        plugin.getArenaConfiguration().save();
        dmSpawnPoints.clear();
        for (int i = 1; i <= spawnDM; i++) {
            dmSpawnPoints.add(Tools.stringToLoc(plugin.getArenaConfiguration().getArenaConfig().getString("dmSpawnPoints." + i)));
        }
        plugin.getMessagesController().sendMessage(player, "&7Has puesto el deathmatch spawn &e&l" + spawnDM);
    }

    public void getNextSpawnPoint(Player player) {
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            player.teleport(spawnPoints.get(lastSpawnGiven));
            spawnPoints.remove(lastSpawnGiven);
            plugin.getGameManager().getPlayers().stream().forEach((p) -> {
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1F, 1F);
            });
        });
        lastSpawnGiven++;
        if (lastSpawnGiven >= spawnPoints.size()) {
            lastSpawnGiven = 0;
        }
    }

    private void removeItems() {
        plugin.getArenaWorld().getEntities().stream().filter((entity) -> (entity.getType() == EntityType.ARROW || entity.getType() == EntityType.DROPPED_ITEM || entity.getType() == EntityType.EXPERIENCE_ORB)).forEach((entity) -> {
            entity.remove();
        });
    }

    public Location getLobby() {
        return lobby;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public String getServerID() {
        return serverID;
    }

    public boolean isSpawnPointsSet() {
        return (spawnPoints != null && !spawnPoints.isEmpty());
    }

    public int getLastSpawnGiven() {
        return lastSpawnGiven;
    }

    public void setLastSpawnGiven(int lastSpawnGiven) {
        this.lastSpawnGiven = lastSpawnGiven;
    }

}
