package uk.figgyc.mcpermstodiscordroles;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import static sx.blah.discord.api.internal.DiscordEndpoints.APIBASE;
import static sx.blah.discord.api.internal.DiscordEndpoints.OAUTH;

public class CommandLinkDiscord implements CommandExecutor {
    Main plugin;
    OkHttpClient client;

    public CommandLinkDiscord(Main main) {
        this.plugin = main;
        this.client = new OkHttpClient();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!plugin.dataConfig.getConfigurationSection("users").contains(player.getUniqueId().toString())) {
                if (args.length > 0) {
                    // code input
                    String code = args[0];
                    try {
                        Request request = new Request.Builder()
                                .url(APIBASE + "/users/@me")
                                .header("Authorization", "Bearer " + code)
                                .header("User-Agent", "DiscordBot (https://github.com/figgyc/MCPermsToDiscordRoles, 0.1)")
                                .build();

                        Response response = client.newCall(request).execute();
                        String body = response.body().string();
                        JSONObject jsonObject = new JSONObject(body);
                        long id = Long.parseLong(jsonObject.getString("id"));
                        plugin.dataConfig.getConfigurationSection("users").set(player.getUniqueId().toString(), id);
                        plugin.updateRole(player);
                        player.sendMessage("Account link successful!");
                    } catch (Exception e) {
                        e.printStackTrace();
                        player.sendMessage("Account link error! Try again.");

                    }

                } else {
                    // generate auth url
                    player.sendMessage("To link your Discord and Minecraft accounts, go to " + OAUTH + "authorize?response_type=token&scope=identify&client_id=" + plugin.config.getString("clientid"));
                }
            } else {
                plugin.removeRoles(player);
                plugin.dataConfig.getConfigurationSection("users").set(player.getUniqueId().toString(), null);

                player.sendMessage("Discord unlinked, type /linkdiscord again to link another account");
            }
        }
        return true;
    }
}
