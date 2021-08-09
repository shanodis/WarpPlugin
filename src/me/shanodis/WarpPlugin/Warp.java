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
        String start = "(Warp plugin): ";
        String errorMessage = start;
        errorMessage += warps.containsValue(location) ? "This location is already added to list!"
                : warps.containsKey(warpName) ? "Given name already exists in warp list!" : "";

        if (!errorMessage.equals(start)) {
            player.sendMessage(errorMessage);
            return;
        }

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

        String message = "(Warp Plugin): Warp " + warpName + " created successfully!";
        player.sendMessage(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + message);

        String bcMessage = ChatColor.LIGHT_PURPLE + start + player.getName() + ChatColor.DARK_PURPLE
                + " has created a new warp " + warpName;
        Bukkit.broadcastMessage(bcMessage);
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

    public void listWarps(Player player) {
        String start = "(Warp plugin): ";
        if (warps.size() == 0) {
            player.sendMessage(ChatColor.RED + start + "Warp list is empty!");
            return;
        }

        player.sendMessage(start + "List of warps:"); // entry message

        int i = 1;
        for (String warp: warps.keySet()) {
            player.sendMessage(i + ": " + warp);
            i++;
        }
    }
}
