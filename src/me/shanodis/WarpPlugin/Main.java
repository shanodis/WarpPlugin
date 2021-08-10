package me.shanodis.WarpPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.function.*;

public class Main extends JavaPlugin {
    private Warp warpHandler;

    @Override
    public void onEnable() {
        File file = new File("Warps/warps.txt");
        try {
            String fileCreatingMessage = file.createNewFile() ? "File already exists." : "Warps file created successfully!";
            getLogger().info(fileCreatingMessage);

            warpHandler = new Warp(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLogger().info("Warp plugin enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Warp plugin disabled.");
    }

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        Player player = (Player) sender;
        String lowerCmd = cmd.getName().toLowerCase();
        Function<String, Boolean> printErrorMessage = alert -> {
            String errorMessage = "(Warp plugin): Failed to execute command. " + alert;
            player.sendMessage(ChatColor.RED + errorMessage);
            return false;
        };

        if (lowerCmd.equals("warplist")) {
            if (args.length == 0) {
                warpHandler.listWarps(player);
                return true;
            } else {
                return printErrorMessage.apply("This command doesn't take any arguments!");
            }
        }

        String cmdArgument = args[0].toUpperCase();
        String oneArgAlert = "This command takes one argument!";
        boolean argsCheck = args.length != 1;

        switch (lowerCmd) {
            case "setwarp" -> {
                if (argsCheck) return printErrorMessage.apply(oneArgAlert);
                warpHandler.createNewWarp(player, cmdArgument);
            }
            case "warp" -> {
                if (argsCheck) return printErrorMessage.apply(oneArgAlert);
                warpHandler.warpPlayer(player, cmdArgument);
            }
            case "warpdel" -> {
                if (argsCheck) return printErrorMessage.apply(oneArgAlert);
                warpHandler.deleteWarp(player, cmdArgument);
            }
        }

        return true;
    }
}
