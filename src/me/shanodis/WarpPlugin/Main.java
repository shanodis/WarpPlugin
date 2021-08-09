package me.shanodis.WarpPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

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
		Player player = (Player)sender;
		String lowerCmd = cmd.getName().toLowerCase();

		if (args.length != 1) {
			String errorMessage = "(Warp plugin): Failed to execute command. This command takes one argument!";
			player.sendMessage(ChatColor.RED + errorMessage);
			return false;
		}

		switch (lowerCmd) {
			case "setwarp" -> warpHandler.createNewWarp(player, args[0].toUpperCase());
			case "warp" -> warpHandler.warpPlayer(player, args[0].toUpperCase());
			case "listwarps" -> warpHandler.listWarps(player);
		}

		return true;
	}
}
