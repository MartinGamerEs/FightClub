package com.isnakebuzz.plugins.task;

import com.isnakebuzz.plugins.Main;
import com.isnakebuzz.plugins.utils.Tools;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathMatchTask extends BukkitRunnable {
    
    private final Main plugin;
    
    public DeathMatchTask(Main plugin) {
        this.plugin = plugin;
    }
    
    private int count = 200;
    
    @Override
    public void run() {
        if(plugin.getGameManager().isDeathmatchStarted() == true){
            this.cancel();
        }
        plugin.getPlayerManager().getBoard().setName("§b§lFightClub §6" + Tools.transform(count));
        if (plugin.getGameManager().isEnding()) {
            this.cancel();
        }
        if (count == 200) {
            
        } else if (count == 0) {
            cancel();
        }
        --count;
        
    }
    
}
