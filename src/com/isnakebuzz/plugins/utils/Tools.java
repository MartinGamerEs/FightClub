package com.isnakebuzz.plugins.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;

public class Tools {

    public static void firework(Location loc, Color color1, Color color2, Color color3, FireworkEffect.Type type) {
        final World world = loc.getWorld();

        for (int i = -2; i < 3; i++) {
            org.bukkit.entity.Firework firework = world.spawn(new org.bukkit.Location(loc.getWorld(), loc.getX() + (i * 5), loc.getY(), loc.getZ()), org.bukkit.entity.Firework.class);
            org.bukkit.inventory.meta.FireworkMeta data = firework.getFireworkMeta();
            data.addEffects(org.bukkit.FireworkEffect.builder()
                    .withColor(color1).withColor(color2).withColor(color3).with(type)
                    .trail(new java.util.Random().nextBoolean()).flicker(new java.util.Random().nextBoolean()).build());
            data.setPower(new java.util.Random().nextInt(2) + 2);
            firework.setFireworkMeta(data);
        }
    }

    public static String locationToString(Location l) {
        return String.valueOf(new StringBuilder(String.valueOf(l.getWorld().getName())).append(":").append(l.getBlockX()).toString()) + ":" + String.valueOf(l.getBlockY()) + ":" + String.valueOf(l.getBlockZ());
    }

    public static Location stringToLoc(String s) {
        Location l = null;
        try {
            World world = Bukkit.getWorld(s.split(":")[0]);
            Double x = Double.parseDouble(s.split(":")[1]);
            Double y = Double.parseDouble(s.split(":")[2]);
            Double z = Double.parseDouble(s.split(":")[3]);

            l = new Location(world, x + 0.5, y, z + 0.5);
        } catch (Exception ex) {

        }
        return l;
    }

    public static String transform(double time) {
        boolean negative = false;
        if (time < 0) {
            negative = true;
            time *= -1;
        }
        int hours = (int) time / 3600;
        int minutes = (int) (time - (hours * 3600)) / 60;
        int seconds = (int) time - (hours * 3600) - (minutes * 60);
        String hoursString = hours + "";
        String minutesString = minutes + "";
        String secondsString = seconds + "";
        while (minutesString.length() < 2) {
            minutesString = "0" + minutesString;
        }
        while (secondsString.length() < 2) {
            secondsString = "0" + secondsString;
        }
        return (negative ? "-" : "") + (hours == 0 ? "" : hoursString + ":") + minutesString + ":" + secondsString;
    }

}
