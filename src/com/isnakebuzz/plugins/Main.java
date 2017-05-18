package com.isnakebuzz.plugins;

import com.isnakebuzz.plugins.utils.inv.GameController;
import com.isnakebuzz.plugins.utils.MessagesController;
import com.isnakebuzz.plugins.utils.ItemLoader;
import com.isnakebuzz.plugins.manager.enums.GameState;
import com.isnakebuzz.plugins.manager.config.ArenaConfiguration;
import com.isnakebuzz.plugins.manager.ArenaManager;
import com.isnakebuzz.plugins.manager.PlayerManager;
import com.isnakebuzz.plugins.manager.GameManager;
import com.isnakebuzz.plugins.manager.GameControllerManager;
import com.isnakebuzz.plugins.listener.PlayerListener;
import com.isnakebuzz.plugins.listener.ServerListener;
import com.isnakebuzz.plugins.listener.GameListener;
import org.bukkit.plugin.java.*;
import org.bukkit.*;
import java.util.logging.*;
import java.io.*;
import org.bukkit.configuration.*;
import org.bukkit.plugin.*;

public class Main extends JavaPlugin
{
    private final GameListener gl;
    private final PlayerListener pl;
    private final ServerListener sl;
    private final ArenaConfiguration ac;
    private final GameManager gm;
    private final ArenaManager am;
    private final PlayerManager pm;
    private final GameControllerManager sm;
    private final MessagesController msgc;
    private final CommandManager cm;
    private final GameController cs;
    private World arenaWorld;
    private boolean editMode;
    
    public Main() {
        this.ac = new ArenaConfiguration(this);
        this.gm = new GameManager(this);
        this.am = new ArenaManager(this);
        this.pm = new PlayerManager(this);
        this.sm = new GameControllerManager(this);
        this.msgc = new MessagesController(this);
        this.cm = new CommandManager(this);
        this.gl = new GameListener(this);
        this.pl = new PlayerListener(this);
        this.sl = new ServerListener(this);
        this.cs = new GameController(this);
    }
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.editMode = this.getConfig().getBoolean("editMode");
        this.arenaWorld = this.getServer().getWorld(this.getConfig().getString("world"));
        this.msgc.init();
        this.cm.init();
        try {
            this.ac.init();
        }
        catch (IOException | InvalidConfigurationException ex3) {
            final Exception ex2;
            final Exception ex = ex3;
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (this.editMode) {
            this.getLogger().log(Level.INFO, "FFC: Editmode activado correctamente");
        }
        else if (!this.editMode) {
            ItemLoader.init();
            this.am.init();
            this.gm.init();
            this.gl.init();
            this.pl.init();
            this.sl.init();
            this.sm.init();
            this.cs.init();
            this.arenaWorld.setAutoSave(false);
            GameState.state = GameState.LOBBY;
            this.getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
        }
        this.getLogger().log(Level.INFO, "FFC: Activado correctamente");
    }
    
    public void onDisable() {
        if (!this.editMode) {
            this.gl.unregisterAllGameEvents();
            this.pl.unregisterAllPlayerEvents();
            this.sl.unregisterAllServerEvents();
        }
        this.getLogger().log(Level.INFO, "FFC: Desactivado correctamente");
    }
    
    public boolean isEditMode() {
        return this.editMode;
    }
    
    public World getArenaWorld() {
        return this.arenaWorld;
    }
    
    public ArenaConfiguration getArenaConfiguration() {
        return this.ac;
    }
    
    public GameManager getGameManager() {
        return this.gm;
    }
    
    public ArenaManager getArenaManager() {
        return this.am;
    }
    
    public PlayerManager getPlayerManager() {
        return this.pm;
    }
    
    public GameControllerManager getSkillManager() {
        return this.sm;
    }
    
    public MessagesController getMessagesController() {
        return this.msgc;
    }
}
