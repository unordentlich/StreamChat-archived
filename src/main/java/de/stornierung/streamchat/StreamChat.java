package de.stornierung.streamchat;

import de.stornierung.streamchat.api.TwitchBot;
import de.stornierung.streamchat.listener.IngameChat;
import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ServerData;
import net.minecraft.client.Minecraft;

import java.util.List;

public class StreamChat extends LabyModAddon {
    public static boolean enabled;
    public static String twitchChannel;
    public static String ingamePrefix;
    public static String prefix;
    public static boolean twitchenabled;
    public static boolean chatenabled;
    public static String twitchtoken;
    public static TwitchBot bot;

    public static boolean server_cmd = true;
    public static boolean name_cmd = true;
    public static boolean version_cmd = true;
    public static boolean token_working = false;

    @Override
    public void onEnable() {
        System.out.println("## StreamChat+ | by unordentlich ##");

        this.getApi().registerForgeListener(this);
        this.getApi().getEventManager().register(new IngameChat());
        this.getApi().getEventManager().registerOnJoin(new Consumer<ServerData>() {
            @Override
            public void accept(ServerData serverData) {
                TwitchBot.startBot();
                if (twitchenabled) {
                    if (!token_working) {
                        System.out.println("StreamChat+ | Addon couldn't connect to your twitch profile! :(");
                        LabyMod.getInstance().displayMessageInChat("§eStream§7Chat§6+ §8» §cAddon couldn't connect to your twitch profile! :(");
                    }
                }
            }
        });

        this.getApi().getEventManager().registerOnQuit(new Consumer<ServerData>() {
            @Override
            public void accept(ServerData serverData) {
                TwitchBot.stopBot();
            }
        });
    }

    @Override
    public void loadConfig() {
        if (!getConfig().has("enabled")) getConfig().addProperty("enabled", true);
        if (!getConfig().has("twitchtoggle")) getConfig().addProperty("twitchtoggle", true);
        if (!getConfig().has("twitchchannel")) getConfig().addProperty("twitchchannel", "");
        if (!getConfig().has("prefix")) getConfig().addProperty("prefix", "§5✪ §d%user% §8> §7%message%");
        if (!getConfig().has("twitchtoken")) getConfig().addProperty("twitchtoken", "");
        if (!getConfig().has("chattoggle")) getConfig().addProperty("chattoggle", false);
        if (!getConfig().has("chatprefix")) getConfig().addProperty("chatprefix", "##");
        enabled = getConfig().get("enabled").getAsBoolean();
        twitchenabled = getConfig().get("twitchtoggle").getAsBoolean();
        twitchChannel = getConfig().get("twitchchannel").getAsString();
        prefix = getConfig().get("prefix").getAsString();
        twitchtoken = getConfig().get("twitchtoken").getAsString();
        chatenabled = getConfig().get("chattoggle").getAsBoolean();
        ingamePrefix = getConfig().get("chatprefix").getAsString();
    }

    @Override
    protected void fillSettings(List<SettingsElement> subSettings) {
        /* HEADER */
        subSettings.add(new HeaderElement("§7— §eStream§7Chat§6§l+ §7—", 2));
        subSettings.add(new HeaderElement("§7Support? §8» §ejonas#6789"));

        /* ENABLED TOGGLE */
        subSettings.add(new BooleanElement("Enabled",
                new ControlElement.IconData(Material.EMERALD), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean enabled) {
                StreamChat.enabled = enabled;

                StreamChat.this.getConfig().addProperty("enabled", enabled);
                StreamChat.this.saveConfig();
                loadConfig();
            }
        }, enabled));

        /* PREFIX */
        StringElement prefixStringElement = new StringElement("Prefix",
                new ControlElement.IconData(Material.BOOK),
                prefix.replace("§", "&"), new Consumer<String>() {
            @Override
            public void accept(String prefix) {
                StreamChat.prefix = prefix.replace("&", "§");

                StreamChat.this.getConfig().addProperty("prefix", prefix.replace("&", "§"));
                StreamChat.this.saveConfig();
                loadConfig();
            }
        });
        subSettings.add(prefixStringElement);

        /* TWITCH TITLE */
        HeaderElement TwitchHeader = new HeaderElement("§8» §d✪ §5Twitch §7Settings §8«");
        subSettings.add(TwitchHeader);

        /* TWITCH TOGGLE */
        subSettings.add(new BooleanElement("Enabled",
                new ControlElement.IconData(Material.REDSTONE), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean enabled) {
                twitchenabled = enabled;

                StreamChat.this.getConfig().addProperty("twitchtoggle", enabled);
                StreamChat.this.saveConfig();
                loadConfig();
            }
        }, twitchenabled));

        /* TWITCH USERNAME */
        StringElement channelStringElement = new StringElement("Twitch Username",
                new ControlElement.IconData(Material.PAPER),
                twitchChannel, new Consumer<String>() {
            @Override
            public void accept(String username) {
                twitchChannel = username;

                StreamChat.this.getConfig().addProperty("twitchchannel", username);
                StreamChat.this.saveConfig();
                loadConfig();

                if (Minecraft.getMinecraft().isSingleplayer() || Minecraft.getMinecraft().getCurrentServerData() != null) {
                    LabyMod.getInstance().displayMessageInChat("§eStream§7Chat§6+ §8» §7Please re-enter the server/world to load all changes.");
                }
            }
        });
        subSettings.add(channelStringElement);

        /* TWITCH OAUTH TOKEN */
        StringElement tokenStringElement = new StringElement("Twitch Token",
                new ControlElement.IconData(Material.COMMAND),
                twitchtoken, new Consumer<String>() {
            @Override
            public void accept(String token) {
                twitchtoken = token;

                StreamChat.this.getConfig().addProperty("twitchtoken", token);
                StreamChat.this.saveConfig();
                loadConfig();

                if (Minecraft.getMinecraft().isSingleplayer() || Minecraft.getMinecraft().getCurrentServerData() != null) {
                    LabyMod.getInstance().displayMessageInChat("§eStream§7Chat§6+ §8» §7Please re-enter the server/world to load all changes.");
                }
            }
        });
        subSettings.add(tokenStringElement);

        /* TWITCH TITLE */
        HeaderElement placeholder = new HeaderElement(" ");
        subSettings.add(placeholder);

        /* TWITCH-INGAME CHAT TOGGLE */
        subSettings.add(new BooleanElement("Chat via Ingame-Messages",
                new ControlElement.IconData(Material.MAP), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean enabled) {
                chatenabled = enabled;

                StreamChat.this.getConfig().addProperty("chattoggle", enabled);
                StreamChat.this.saveConfig();
                loadConfig();
            }
        }, chatenabled));

        /* INGAME CHATTER */
        StringElement ingameChatPrefix = new StringElement("Chat Prefix",
                new ControlElement.IconData(Material.PAPER),
                ingamePrefix, new Consumer<String>() {
            @Override
            public void accept(String prefix) {
                ingamePrefix = prefix;

                StreamChat.this.getConfig().addProperty("chatprefix", prefix);
                StreamChat.this.saveConfig();
                loadConfig();
            }
        });
        subSettings.add(ingameChatPrefix);

        /* SUPPORT NOTICE */
        subSettings.add(new HeaderElement("§7§oYou can get your twitch token from https://twitchapps.com/tmi/", 0.7));
    }
}
