package com.isnakebuzz.plugins.manager;

import java.util.ArrayList;
import java.util.Random;
import com.isnakebuzz.plugins.Main;
import com.isnakebuzz.plugins.utils.ItemLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameControllerManager {

    private final Main plugin;

    public GameControllerManager(Main plugin) {
        this.plugin = plugin;
        _enchanterSkill = new ArrayList<>();
        _wizardSkill = new ArrayList<>();
        this._naturalRegeneration = new ArrayList<>();
        this._uhcRegeneration = new ArrayList<>();
        normalVida = new ArrayList<>();
        dobleVida = new ArrayList<>();
        triplevida = new ArrayList<>();
        rand = new Random();
    }

    private final ArrayList<Player> _enchanterSkill;
    private final ArrayList<Player> _wizardSkill;

    private final ArrayList<Player> _naturalRegeneration;
    private final ArrayList<Player> _uhcRegeneration;

    private final ArrayList<Player> normalVida;
    private final ArrayList<Player> dobleVida;
    private final ArrayList<Player> triplevida;

    private final Random rand;

    public void addVoteHealth(Player player, ArrayList toVote, ArrayList contais, ArrayList contais2) {
        if (toVote.contains(player)) {
            plugin.getMessagesController().sendMessage(player, "&cYa votaste por esta opción.");
        } else if (contais.contains(player) || contais2.contains(player)) {
            plugin.getMessagesController().sendMessage(player, "&cYa has votado por otra opción");
        } else {
            toVote.add(player);
        }
    }

    public ArrayList<Player> getNormalVida() {
        return normalVida;
    }

    public ArrayList<Player> getDobleVida() {
        return dobleVida;
    }

    public ArrayList<Player> getTriplevida() {
        return triplevida;
    }
    
    

    public void addVoteNormalVida(Player player) {
        addVoteHealth(player, normalVida, dobleVida, triplevida);
    }

    public void addVoteDobleVida(Player player) {
        addVoteHealth(player, dobleVida, normalVida, triplevida);
    }

    public void addVotrTripleVida(Player player) {
        addVoteHealth(player, triplevida, normalVida, dobleVida);
    }

    public void checkVida() {
        if (normalVida.size() >= 1 && dobleVida.size() < normalVida.size() && triplevida.size() < normalVida.size()) {
            plugin.getMessagesController().sendBroadcast("&c&lMODIFACIONES: &e&lVIDA NORMAL");
        } else if (dobleVida.size() >= 1 && normalVida.size() < dobleVida.size() && triplevida.size() < dobleVida.size()) {
            plugin.getGameManager().getPlayers().stream().map((players) -> {
                players.setMaxHealth(40);
                return players;
            }).forEach((players) -> {
                players.setHealth(players.getMaxHealth());
            });
            plugin.getMessagesController().sendBroadcast("&c&lMODIFACIONES: &e&lVIDA DOBLE");
        } else if (triplevida.size() >= 1 && normalVida.size() < triplevida.size() && normalVida.size() < triplevida.size()) {
            plugin.getGameManager().getPlayers().stream().map((players) -> {
                players.setMaxHealth(50);
                return players;
            }).forEach((players) -> {
                players.setHealth(players.getMaxHealth());
            });
            plugin.getMessagesController().sendBroadcast("&c&lMODIFACIONES: &e&lVIDA TRIPLE");
        } else if (normalVida.isEmpty() && dobleVida.isEmpty() && triplevida.isEmpty()) {
            plugin.getMessagesController().sendBroadcast("&c&lMODIFACIONES: &e&lVIDA NORMAL");
        } else {
            plugin.getMessagesController().sendBroadcast("&c&lMODIFACIONES: &e&lVIDA NORMAL");
        }
    }

    public void checkRegeneracion() {
        if (_naturalRegeneration.size() > _uhcRegeneration.size()) {
            plugin.getMessagesController().sendBroadcast("&c&lMODIFACIONES: &e&lRegeneración VANILLA");
        } else if (_uhcRegeneration.size() > _naturalRegeneration.size()) {
            plugin.getArenaWorld().setGameRuleValue("naturalRegeneration", "false");
            plugin.getMessagesController().sendBroadcast("&c&lMODIFACIONES: &e&lRegeneración UHC");
        } else if (_naturalRegeneration.size() == _uhcRegeneration.size()) {
            plugin.getArenaWorld().setGameRuleValue("naturalRegeneration", "true");
            plugin.getMessagesController().sendBroadcast("&c&lMODIFACIONES: &e&lRegeneración VANILLA");
        } else {
            plugin.getMessagesController().sendBroadcast("&c&lMODIFACIONES: &e&lRegeneración VANILLA");
            plugin.getArenaWorld().setGameRuleValue("naturalRegeneration", "true");
        }
    }

    public void init() {
        _enchanterSkill.clear();
        _wizardSkill.clear();
        _naturalRegeneration.clear();
        _uhcRegeneration.clear();
    }

    public void addVoteToNormal(Player player) {
        if (_naturalRegeneration.contains(player)) {
            plugin.getMessagesController().sendMessage(player, "&cYa votaste por esta opción.");
        } else if (_uhcRegeneration.contains(player)) {
            plugin.getMessagesController().sendMessage(player, "&cYa votaste por una opción.");
        } else {
            _naturalRegeneration.add(player);
            plugin.getMessagesController().sendMessage(player, "&cHas votado por &e&lRegeneración VANILLA");
            plugin.getMessagesController().sendBroadcast("&6" + player.getDisplayName() + " &7votó por &eregeneración VANILLA&7.");
            plugin.getGameManager().getPlayers().stream().forEach((players) -> {
            plugin.getMessagesController().sendTitle(players, "&e&lRegeneración VANILLA", "&6" + player.getDisplayName() + " &7votó por esta opción", 0, 0, 0);
            });
        }
    }

    public void addVoteToUHC(Player player) {
        if (_uhcRegeneration.contains(player)) {
            plugin.getMessagesController().sendMessage(player, "&cYa votaste por esta opción.");
        } else if (_naturalRegeneration.contains(player)) {
            plugin.getMessagesController().sendMessage(player, "&cYa votaste por una opción.");
        } else {
            _uhcRegeneration.add(player);
            plugin.getMessagesController().sendMessage(player, "&cHas votado por &e&lRegeneración VANILLA");
            plugin.getMessagesController().sendBroadcast("&6" + player.getDisplayName() + " &7votó por &eregeneración VANILLA&7.");
            plugin.getGameManager().getPlayers().stream().forEach((players) -> {
                plugin.getMessagesController().sendTitle(players, "&eRegeneración VANILLAs", "&6" + player.getDisplayName() + " &7votó por esta opción", 0, 0, 0);
            });
        }
    }

    public void setReward(PlayerDeathEvent e) {
        if (plugin.getGameManager().isInGame()) {
            if (e.getEntity().getKiller() instanceof Player) {
                if (_enchanterSkill.contains(e.getEntity().getKiller())) {
                    int intran = rand.nextInt(10);
                    if (intran <= 5) {
                        e.getEntity().getKiller().giveExp(intran);
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            plugin.getMessagesController().sendActionBar(e.getEntity().getKiller(), "&6+" + intran + " &7de XP");
                        });
                    } else {
                        e.getEntity().getKiller().giveExp(intran);
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            plugin.getMessagesController().sendActionBar(e.getEntity().getKiller(), "&6+" + intran + " &7de XP");
                        });
                    }
                } else if (_wizardSkill.contains(e.getEntity().getKiller())) {
                    int pro = rand.nextInt(20);
                    if (pro <= 10) {
                        e.getEntity().getKiller().getInventory().addItem(ItemLoader.getEs1());
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            plugin.getMessagesController().sendActionBar(e.getEntity().getKiller(), "&6+1 &7libro de agudeza");
                        });
                    } else {
                        e.getEntity().getKiller().getInventory().addItem(ItemLoader.getEs1());
                        e.getEntity().getKiller().getInventory().addItem(ItemLoader.getGoldenApple());
                        e.getEntity().getKiller().getInventory().addItem(ItemLoader.getPr1());
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            plugin.getMessagesController().sendActionBar(e.getEntity().getKiller(), "&6+1 &7libro de agudeza, golden apple, poción de regeneración");
                        });
                    }
                }
            }
        }
    }

    public void addPlayerToWizardSkill(Player player) {
        if (_enchanterSkill.contains(player) || _wizardSkill.contains(player)) {
            _enchanterSkill.remove(player);
            _wizardSkill.remove(player);
            _wizardSkill.add(player);
        } else {
            _wizardSkill.add(player);
        }
    }

    public void addPlayerToEnchanterSkill(Player player) {
        if (_enchanterSkill.contains(player) || _wizardSkill.contains(player)) {
            _enchanterSkill.remove(player);
            _wizardSkill.remove(player);

            _enchanterSkill.add(player);
        } else {
            _enchanterSkill.add(player);
        }
    }

    public ArrayList<Player> getNaturalRegeneration() {
        return _naturalRegeneration;
    }

    public ArrayList<Player> getUhcRegeneration() {
        return _uhcRegeneration;
    }

}
