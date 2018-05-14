# MCPermsToDiscordRoles
Link your Discord server to your Minecraft server. This works by giving people with certain permissions certain roles on Discord.

## Setup
[Create a Discord application](https://discordapp.com/developers/applications/me/create) and add a bot to it.
Set the redirect URL to https://figgyc.uk/mcpermscallback or if you can save that page and rebrand it and host it yourself.
The bot will be on your server so you should add a relevant name and picture. The application name will be seen by people when they are linking their accounts so keep this in mind.

On the application page, open the OAuth2 URL Generator and make sure the bot scope and the Manage Roles bot permission are checked, then visit the link to add the bot to your server.
Open `config.yml`. Copy in the bot token and client ID from the application page. Copy the guild ID from Discord by right clicking your server icon in Discord and clicking Copy ID.

You will need to have a permission for each role in Discord. The configuration of the permissions section is fairly self explanatory, however finding the role ID is hard.
The easiest way to do it is to create a private channel, set the role to mentionable then type `\@rolename` in chat. This is annoying though and if you have another bot on the server then you should check if it has the capability to list role IDs.

## Usage
Type /linkdiscord to link or unlink an account and the rest is fairly self explanatory. Roles are refreshed for a user whenever they log in and you can manually refresh the roles for all online users with /refreshdiscord.

## Permissions
`mcpermstodiscordrules.linkdiscord` allows usage of /linkdiscord

`mcpermstodiscordrules.refreshdiscord` allows usage of /refreshdiscord