package com.isnakebuzz.plugins.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.isnakebuzz.plugins.Main;
import com.isnakebuzz.plugins.manager.enums.FaseState;
import com.isnakebuzz.plugins.utils.ItemLoader;
import com.isnakebuzz.plugins.utils.ScoreboardUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerManager {

    private final Main plugin;

    public PlayerManager(Main plugin) {
        this.plugin = plugin;
    }

    private ScoreboardUtil board;
    
    public void sendInfo(){
        for(Player players : plugin.getServer().getOnlinePlayers()){
            plugin.getMessagesController().sendColorMessage(players, " ");
            plugin.getMessagesController().sendColorMessage(players, "                  &3FIGHT&c&lCLUB       ");
            plugin.getMessagesController().sendColorMessage(players, "&7La partida ha finalizado y ganador fue &6" + plugin.getGameManager().getWinnerName());
            plugin.getMessagesController().sendColorMessage(players, "&7Plugin desarrollado por &cMartinGamer_Es&7.");
            plugin.getMessagesController().sendColorMessage(players, "            &6&lmc.doricrafters.net          ");
            plugin.getMessagesController().sendColorMessage(players, "           &etienda.doricrafters.net         ");
            plugin.getMessagesController().sendColorMessage(players, " ");
        }
    }

    public void sendToServer(Player player) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF("lobby");

        } catch (IOException ex) {
            Logger.getLogger(Main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
    }

    public void setDefaultKit(Player player) {
        setCleanPlayer(player, GameMode.SURVIVAL);
        player.getInventory().setHelmet(ItemLoader.getIronHelmet());
        player.getInventory().setChestplate(ItemLoader.getIronChestplate());
        player.getInventory().setLeggings(ItemLoader.getIronLeggins());
        player.getInventory().setBoots(ItemLoader.getIronBoots());
        player.getInventory().setItem(0, ItemLoader.getSword());
        player.getInventory().setItem(1, ItemLoader.getFood());
    }

    public void addReward(Player player) {
        switch (FaseState.fase) {
            case FASE_1:
                player.getInventory().addItem(ItemLoader.getXp());
                player.getInventory().addItem(ItemLoader.getLapiz());
                player.getInventory().addItem(ItemLoader.getEp1());
                player.getInventory().addItem(ItemLoader.getEp2());
                player.getInventory().addItem(ItemLoader.getEs1());
                player.getInventory().addItem(ItemLoader.getPr1());
                player.getInventory().addItem(ItemLoader.getPd1());
                player.getInventory().addItem(ItemLoader.getEbp1());
                player.getInventory().addItem(ItemLoader.getBow());
                player.getInventory().addItem(ItemLoader.getArrow());
                break;
            case FASE_2:
                player.getInventory().addItem(ItemLoader.getXp2());
                player.getInventory().addItem(ItemLoader.getGoldenApple());
                player.getInventory().addItem(ItemLoader.getP2());
                player.getInventory().addItem(ItemLoader.getS2());
                player.getInventory().addItem(ItemLoader.getIh2());
                player.getInventory().addItem(ItemLoader.getLp2());
                player.getInventory().addItem(ItemLoader.getArrow2());
                player.getInventory().addItem(ItemLoader.getRod());
                break;
            case FASE_3:
                player.getInventory().addItem(ItemLoader.getXp());
                player.getInventory().addItem(ItemLoader.getLapiz());
                player.getInventory().addItem(ItemLoader.getGoldenApple());
                player.getInventory().addItem(ItemLoader.getGoldenApple());
                player.getInventory().addItem(ItemLoader.getP2());
                player.getInventory().addItem(ItemLoader.getEp2());
                player.getInventory().addItem(ItemLoader.getBd3());
                player.getInventory().addItem(ItemLoader.getS3());
                player.getInventory().addItem(ItemLoader.getArrow());
                player.getInventory().addItem(ItemLoader.getIh2());
                break;
        }
    }

    public void setLobbyPlayer(Player player) {
        setupScoreboard(player);
        setCleanPlayer(player, GameMode.ADVENTURE);
        player.teleport(plugin.getArenaManager().getLobby());
    }

    public void setSpectator(Player player) {
        plugin.getGameManager().addSpectator(player);
        setCleanPlayer(player, GameMode.SPECTATOR);
        plugin.getMessagesController().sendTitle(player, "&4&nHas Muerto", "&aSuerte para la proxima :(", 5, 10, 5);
        plugin.getGameManager().getPlayers().stream().forEach((players) -> {
            players.hidePlayer(player);
        });
    }

    public void setCleanPlayer(Player player, GameMode gamemode) {
        player.setMaxHealth(20);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setExp(0f);
        player.setTotalExperience(0);
        player.setLevel(0);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getActivePotionEffects().stream().forEach((effect) -> {
            player.removePotionEffect(effect.getType());
        });
        player.setGameMode(gamemode);
    }

    public void setupScoreboard(Player player) {
        board = new ScoreboardUtil("§b§lFightClub ");
        new BukkitRunnable() {
            @Override
            public void run() {
                board.text(5, "§0");
                board.text(4, "§aJugadores§8: §e" + plugin.getGameManager().getPlayers().size());
                board.text(3, "§F");
                board.text(2, "§aServidor§8: §e" + plugin.getArenaManager().getServerID());
                board.text(1, "§6");
                board.text(0, "§6mc.doricrafters.net");
                board.build(player);
            }
        }.runTaskTimer(plugin, 20l, 20l);
    }

    public ScoreboardUtil getBoard() {
        return board;
    }

}
