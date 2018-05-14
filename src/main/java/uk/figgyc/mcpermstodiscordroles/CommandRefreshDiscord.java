package uk.figgyc.mcpermstodiscordroles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandRefreshDiscord implements CommandExecutor {
    Main plugin;

    public CommandRefreshDiscord(Main main) {
        this.plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.updateAllRoles();
        sender.sendMessage("Discord roles refreshed for all online players!");

        return true;
    }
}
