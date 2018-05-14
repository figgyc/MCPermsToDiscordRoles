package uk.figgyc.mcpermstodiscordroles;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public class Main extends JavaPlugin implements Listener {

    FileConfiguration config;
    File configFile;
    FileConfiguration dataConfig;
    File dataConfigFile;

    IDiscordClient client;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        configFile = new File(this.getDataFolder(), "config.yml");
        config = new YamlConfiguration();
        config.options().pathSeparator('/');
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        dataConfigFile = new File(this.getDataFolder(), "data.yml");
        dataConfig = YamlConfiguration.loadConfiguration(dataConfigFile);
        if (!dataConfig.isConfigurationSection("users")) {
            dataConfig.createSection("users");
        }
        try {
            dataConfig.save(dataConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
        clientBuilder.withToken(config.getString("token")); // Adds the login info to the builder
        try {
            client = clientBuilder.login(); // Creates the client instance and logs the client in
        } catch (DiscordException e) { // This is thrown if there was a problem building the client
            e.printStackTrace();
        }



        this.getCommand("linkdiscord").setExecutor(new CommandLinkDiscord(this  ));
        this.getCommand("refreshdiscord").setExecutor(new CommandRefreshDiscord(this));
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        try {
            dataConfig.save(dataConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAllRoles() {
        for (Player player : this.getServer().getOnlinePlayers()) {
            if (this.dataConfig.getConfigurationSection("users").contains(player.getUniqueId().toString())) {
                updateRole(player);
            }
        }

    }

    public void updateRole(Player player) {
        long discordUser = this.dataConfig.getConfigurationSection("users").getLong(player.getUniqueId().toString());
        if (discordUser != 0) {
            IUser user = client.fetchUser(discordUser);
            for (String permission : this.config.getConfigurationSection("permissions").getKeys(false)) {
                if (player.hasPermission(permission)) {
                    user.addRole(client.getGuildByID(this.config.getLong("guild")).getRoleByID(this.config.getConfigurationSection("permissions").getLong(permission)));
                } else {
                    user.removeRole(client.getGuildByID(this.config.getLong("guild")).getRoleByID(this.config.getConfigurationSection("permissions").getLong(permission)));

                }
            }
        }
    }

    public void removeRoles(Player player) {
        long discordUser = this.dataConfig.getConfigurationSection("users").getLong(player.getUniqueId().toString());
        if (discordUser != 0) {
            IUser user = client.fetchUser(discordUser);
            for (String permission : this.config.getConfigurationSection("permissions").getKeys(false)) {
                user.removeRole(client.getGuildByID(this.config.getLong("guild")).getRoleByID(this.config.getConfigurationSection("permissions").getLong(permission)));
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateRole(event.getPlayer());
    }

}
