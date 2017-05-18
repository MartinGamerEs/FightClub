package com.isnakebuzz.plugins;

import com.isnakebuzz.plugins.utils.Tools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    private final Main plugin;

    public CommandManager(Main plugin) {
        this.plugin = plugin;
    }

    public void init() {
        plugin.getCommand("game").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (cmd.getName().equalsIgnoreCase("game")) {
            if (player == null) {
                plugin.getMessagesController().sendMessageToSender(sender, "&c¡Necesitas ser un jugador!.");
            } else if (player.hasPermission("FightClub.Admin.Setup")) {
                if (args.length < 1) {
                    plugin.getMessagesController().sendMessageToSender(sender, "&7Usa &e/game setlobby/setmin/setmax/setspawn/setdmspawn");
                } else {
                    switch (args[0].toLowerCase()) {
                        case "felipe":
                            player.setMaxHealth(40);
                            player.setHealth(player.getMaxHealth());
                            break;
                        case "setlobby":
                            plugin.getArenaConfiguration().getArenaConfig().set("lobby", Tools.locationToString(player.getLocation()));
                            plugin.getArenaConfiguration().save();
                            plugin.getMessagesController().sendMessageToSender(sender, "&7El &a&lLOBBY &7será en " + Tools.locationToString(player.getLocation()));
                            break;
                        case "setmin":
                            if (args.length < 1) {
                                plugin.getMessagesController().sendMessageToSender(sender, "67Usa &e/game setmin CANTIDAD");
                            } else {
                                int minPlayers = Integer.valueOf(args[1]);
                                plugin.getArenaConfiguration().getArenaConfig().set("min", minPlayers);
                                plugin.getArenaConfiguration().save();
                                plugin.getMessagesController().sendMessageToSender(sender, "&7El mínimo de jugadores será de &a&l" + minPlayers);
                            }
                            break;
                        case "setmax":
                            if (args.length < 1) {
                                plugin.getMessagesController().sendMessageToSender(sender, "67Usa &e/game setmax CANTIDAD");
                            } else {
                                int maxPlayers = Integer.valueOf(args[1]);
                                plugin.getArenaConfiguration().getArenaConfig().set("max", maxPlayers);
                                plugin.getArenaConfiguration().save();
                                plugin.getMessagesController().sendMessageToSender(sender, "&7El máximo de jugadores será de &a&l" + maxPlayers);
                            }
                            break;
                        case "setspawn":
                            plugin.getArenaManager().setSpawnLocation(player);
                            break;
                        case "setdmspawn":
                            plugin.getArenaManager().setDMSpawnLocation(player);
                            break;
                        case "start":
                            plugin.getGameManager().forceStart();
                            break;
                    }
                }
            } else {
                plugin.getMessagesController().sendMessageToSender(sender, "&c¡No puedes hacer ésto!.");
            }
        }
        return false;
    }

}
