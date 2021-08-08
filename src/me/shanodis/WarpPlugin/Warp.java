package me.shanodis.WarpPlugin;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Warp {
    private final HashMap<String, Location> warps;
    private final File file;

    public Warp (File file) {
        warps = new HashMap<>();
        this.file = file;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String warpName = scanner.next();
                int[] cords = new int[3];

                for (int i = 0; i < cords.length; i++)
                    cords[i] = scanner.nextInt();

                String worldName = scanner.nextLine();
                World world = Bukkit.getWorld(worldName.trim());
                Location location = new Location(world, cords[0], cords[1], cords[2]);

                warps.put(warpName, location);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createNewWarp(Player player, String warpName) {
        Location location = player.getLocation();
        String errorMessage = warps.containsValue(location) ? "This location is already added to list!"
                : warps.containsKey(warpName) ? "Given name already exists in warp list!" : "";

//        if (!errorMessage.isBlank())
//            return;

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        String worldName = Objects.requireNonNull(location.getWorld()).getName();

        try {
            FileWriter fileWriter = new FileWriter(file, true);
            String data = "%s %d %d %d %s\n".formatted(warpName, x, y, z, worldName);
            fileWriter.write(data);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        warps.put(warpName, location);

        player.sendMessage("Created warp!");
    }

    public void warpPlayer(Player player, String warpName) {
        String start = "(Warp plugin): ";
        if (!warps.containsKey(warpName)) {
            String errorMessage = start + "Failed to execute command. Cannot find given name!";
            player.sendMessage(ChatColor.RED + errorMessage);
            return;
        }

        Location location = warps.get(warpName);
        String message = start + "Teleported to: " + warpName;
        player.teleport(location);
        player.sendMessage(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + message);
    }
}
