package com.isnakebuzz.plugins.task;

import com.isnakebuzz.plugins.Main;
import com.isnakebuzz.plugins.utils.Tools;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class WinnerTask extends BukkitRunnable {

    private final Main plugin;

    public WinnerTask(Main plugin) {
        this.plugin = plugin;
    }

    private int count = 15;

    @Override
    public void run() {
        plugin.getGameManager().getPlayers().stream().forEach((winner) -> {
            Tools.firework(winner.getLocation(), Color.YELLOW, Color.RED, Color.BLUE, FireworkEffect.Type.CREEPER);
        });
        plugin.getPlayerManager().getBoard().setName("§b§lFightClub §6" + Tools.transform(count));
        if (count == 15) {
            plugin.getPlayerManager().sendInfo();
        }else if (count > 0 && count <= 5) {
            plugin.getServer().getOnlinePlayers().stream().forEach((players) -> {
                    plugin.getPlayerManager().sendToServer(players);
                });
        }else if (count == 0) {
          plugin.getServer().shutdown();
          this.cancel();
        }
        -- count;
    }

}
