/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isnakebuzz.plugins.utils;

import java.lang.reflect.Field;
import com.isnakebuzz.plugins.Main;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class MessagesController {

    private final Main plugin;

    public MessagesController(Main plugin) {
        this.plugin = plugin;
    }

    private String prefix;

    public void init() {
        prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
    }

    public void sendMessage(Player player, String msg) {
        player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void sendMessageToSender(CommandSender sender, String msg) {
        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void sendColorMessage(Player player, String msg) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void sendColorMessageToSender(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void sendBroadcast(String msg) {
        plugin.getServer().broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        CraftPlayer craftplayer = (CraftPlayer) player;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent titleJSON = IChatBaseComponent.ChatSerializer
                .a("{'text': '" + ChatColor.translateAlternateColorCodes('&', title) + "'}");
        IChatBaseComponent subtitleJSON = IChatBaseComponent.ChatSerializer
                .a("{'text': '" + ChatColor.translateAlternateColorCodes('&', subtitle) + "'}");
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleJSON,
                fadeIn, stay, fadeOut);
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                subtitleJSON);
        connection.sendPacket(titlePacket);
        connection.sendPacket(subtitlePacket);
    }

    public void sendActionBar(Player p, String msg) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer
                .a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', msg) + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
    }

    public void sendHeaderAndFooter(Player p, String head, String foot) {
        CraftPlayer craftplayer = (CraftPlayer) p;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent header = IChatBaseComponent.ChatSerializer
                .a("{'color': '', 'text': '" + ChatColor.translateAlternateColorCodes('&', head) + "'}");
        IChatBaseComponent footer = IChatBaseComponent.ChatSerializer
                .a("{'color': '', 'text': '" + ChatColor.translateAlternateColorCodes('&', foot) + "'}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, header);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footer);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
        }
        connection.sendPacket(packet);
    }

    public String getPrefix() {
        return prefix;

    }
}
