package com.isnakebuzz.plugins.manager.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.isnakebuzz.plugins.Main;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ArenaConfiguration {

    private final Main plugin;

    public ArenaConfiguration(Main plugin) {
        this.plugin = plugin;
        arenaConfig = new YamlConfiguration();
    }

    private File arenaFile;
    private final YamlConfiguration arenaConfig;

    public void init() throws IOException, FileNotFoundException,
            InvalidConfigurationException {
        arenaFile = new File(plugin.getDataFolder(), "arena.yml");
        if (!arenaFile.exists()) {
            plugin.saveResource("arena.yml", true);
        }
        arenaConfig.load(arenaFile);
    }

    public void save() {
        try {
            arenaConfig.save(arenaFile);
        } catch (IOException e) {
        }
    }

    public void reload() throws IOException,
            FileNotFoundException, InvalidConfigurationException {
        arenaConfig.load(arenaFile);
    }

    public YamlConfiguration getArenaConfig() {
        return arenaConfig;
    }

}