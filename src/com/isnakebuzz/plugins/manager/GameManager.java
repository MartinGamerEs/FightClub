package com.isnakebuzz.plugins.manager;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import com.isnakebuzz.plugins.Main;
import com.isnakebuzz.plugins.manager.enums.GameState;
import com.isnakebuzz.plugins.task.DeathMatchTask;
import com.isnakebuzz.plugins.task.LobbyTask;
import com.isnakebuzz.plugins.task.WinnerTask;
import org.bukkit.entity.Player;

public class GameManager {

    private final Main plugin;

    public GameManager(Main plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.playersWithKit = new ArrayList<>();
        this.rand = new Random();
        _players_lock = new ReentrantLock(false);
    }

    private final ArrayList<Player> players;
    private final ArrayList<Player> spectators;
    private final ArrayList<Player> playersWithKit;

    public ReentrantLock _players_lock;
    

    private final Random rand;

    private boolean started;
    private boolean deathmatchStarted;

    private String winnerName;

    public void init() {
        players.clear();
        spectators.clear();
        playersWithKit.clear();
        started = false;
        deathmatchStarted = false;
        winnerName = "";
    }

    public void checkTask() {
        if (isInGame()) {
            if (players.size() > 2) {
                int random = rand.nextInt(plugin.getGameManager().getPlayers().size());
                Player player = plugin.getGameManager().getPlayers().get(random);
                player.setHealth(0);
                plugin.getMessagesController().sendBroadcast("&7Ha muerto un jugador para iniciar la deathmatch.");
                checkTask();
            } else if (players.size() == 2 && deathmatchStarted == false) {
                GameState.state = GameState.DEATHMATCH;
                deathmatchStarted = true;
                new DeathMatchTask(plugin).runTaskTimer(plugin, 20l, 20l);
                players.stream().forEach((playersInGame) -> {
                    plugin.getArenaManager().teleport(playersInGame);
                });
            } else if (isInGame() && players.size() == 2 && deathmatchStarted == true) {
                GameState.state = GameState.DEATHMATCH;
                new DeathMatchTask(plugin).runTaskTimer(plugin, 20l, 20l);
                players.stream().forEach((playersInGame) -> {
                    plugin.getArenaManager().teleport(playersInGame);
                });
            } else {
                GameState.state = GameState.DEATHMATCH;
                new DeathMatchTask(plugin).runTaskTimer(plugin, 20l, 20l);
                players.stream().forEach((playersInGame) -> {
                    plugin.getArenaManager().teleport(playersInGame);
                });
            }
        }
    }

    public void checkWin() {
        if (getPlayers().size() < 2) {
            for (Player winner : getPlayers()) {
                winnerName = winner.getDisplayName();
                GameState.state = GameState.ENDING;
                new WinnerTask(plugin).runTaskTimer(plugin, 20l, 20l);
                plugin.getMessagesController().sendBroadcast("&7Ha ganado la paritda &e&l" + winner.getDisplayName());
                plugin.getServer().getOnlinePlayers().stream().forEach((playersInGame) -> {
                    plugin.getMessagesController().sendTitle(playersInGame, "&e&l" + winner.getDisplayName(), "&7ganó la partida", 5, 20, 5);
                });
            }
        }
    }

    public void forceStart() {
        if (started == false) {
            started = true;
            new LobbyTask(plugin).runTaskTimer(plugin, 20l, 20l);
        }
    }

    public void checkDM() {
        if (deathmatchStarted == false && getPlayers().size() == 2) {
            if (isInGame()) {
                deathmatchStarted = true;
            }
        }
    }

    public void addPlayerToGame(Player player) {
        _players_lock.lock();
        try {
            players.remove(player);
        } finally {
           players.add(player);
        }

    }

    public void addSpectator(Player player) {
        spectators.remove(player);
        spectators.add(player);
    }

    public void addPlayerWithKit(Player player) {
        playersWithKit.remove(player);
        playersWithKit.add(player);
    }

    public void removePlayerFromGame(Player player) {
        if (players.contains(player)) {
            players.remove(player);
        }
    }

    public void removeSpectator(Player player) {
        _players_lock.lock();
        try {
            players.remove(player);
        } finally {
            _players_lock.unlock();
        }
    }

    public void removePlayerWithKit(Player player) {
        playersWithKit.remove(player);
    }

    public void setDeathmatchStarted(boolean deathmatchStarted) {
        this.deathmatchStarted = deathmatchStarted;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    
    public boolean isPlayer(Player player){
        boolean ret;
        _players_lock.lock();
        try {
            ret = players.contains(player);
        } finally {
            _players_lock.unlock();
        }
        return ret;
       
    }

    public ArrayList<Player> getSpectators() {
        return spectators;
    }

    public ArrayList<Player> getPlayersWithKit() {
        return playersWithKit;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isNotStarted() {
        return started == false;
    }

    public boolean isDeathmatchStarted() {
        return deathmatchStarted;
    }

    public boolean isEnding() {
        return GameState.state == GameState.ENDING;
    }

    public boolean isInDeathMatch() {
        return GameState.state == GameState.DEATHMATCH;
    }

    public boolean isInGame() {
        return GameState.state == GameState.IN_GAME;
    }

    public boolean isInLobby() {
        return GameState.state == GameState.LOBBY;
    }

}
