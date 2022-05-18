package de.stornierung.streamchat;

import de.stornierung.streamchat.api.TwitchBot;
import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ServerData;

import java.util.List;

public class StreamChat extends LabyModAddon {
    public static boolean enabled;
    public static String twitchchannel;
    public static String prefix;
    public static boolean twitchenabled;
    public static String twitchtoken;
    public static TwitchBot bot;

    public static boolean server_cmd = true;
    public static boolean name_cmd = true;
    public static boolean version_cmd = true;
    public static boolean token_working = false;

    @Override
    public void onEnable() {
        System.out.println("## StreamChat+ | by Stornierung ##");

        this.getApi().registerForgeListener(this);
        this.getApi().getEventManager().registerOnJoin(new Consumer<ServerData>() {
            @Override
            public void accept(ServerData serverData) {
                TwitchBot.startBot();
                if(twitchenabled) {
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
        if(!getConfig().has("enabled")) getConfig().addProperty("enabled", true);
        if(!getConfig().has("twitchtoggle")) getConfig().addProperty("twitchtoggle", true);
        if(!getConfig().has("twitchchannel")) getConfig().addProperty("twitchchannel", "");
        if(!getConfig().has("prefix")) getConfig().addProperty("prefix", "§5✪ §d%user% §8> §7%message%");
        if(!getConfig().has("twitchtoken")) getConfig().addProperty("twitchtoken", "");
        enabled = getConfig().get("enabled").getAsBoolean();
        twitchenabled = getConfig().get("twitchtoggle").getAsBoolean();
        twitchchannel = getConfig().get("twitchchannel").getAsString();
        prefix = getConfig().get("prefix").getAsString();
        twitchtoken = getConfig().get("twitchtoken").getAsString();
    }

    @Override
    protected void fillSettings(List<SettingsElement> subSettings) {
        /* HEADER */
        subSettings.add(new HeaderElement("§7— §eStream§7Chat§6§l+ §7—", 2));
        subSettings.add(new HeaderElement("§7Support? §8» §ejonas#6789"));

        /* ENABLED TOGGLE */
        subSettings.add( new BooleanElement( "Enabled",
                new ControlElement.IconData( Material.EMERALD ), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean enabled) {
                StreamChat.this.enabled = enabled;

                StreamChat.this.getConfig().addProperty("enabled", enabled);
                StreamChat.this.saveConfig();
                loadConfig();
            }
        }, enabled));

        /* PREFIX */
        StringElement prefixStringElement = new StringElement( "Prefix",
                new ControlElement.IconData( Material.BOOK ),
                prefix.replace("§", "&"), new Consumer<String>() {
            @Override
            public void accept(String prefix) {
                StreamChat.this.prefix = prefix.replace("&", "§");

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
                StreamChat.this.twitchenabled = enabled;

                StreamChat.this.getConfig().addProperty("twitchtoggle", enabled);
                StreamChat.this.saveConfig();
                loadConfig();
            }
            }, twitchenabled));

        /* TWITCH USERNAME */
        StringElement channelStringElement = new StringElement( "Twitch Username",
                new ControlElement.IconData( Material.PAPER ),
                twitchchannel, new Consumer<String>() {
            @Override
            public void accept(String username) {
                StreamChat.this.twitchchannel = username;

                StreamChat.this.getConfig().addProperty("twitchchannel", username);
                StreamChat.this.saveConfig();
                loadConfig();

                LabyMod.getInstance().displayMessageInChat("§eStream§7Chat§6+ §8» §7Please re-enter the server/world to load all changes.");
            }
        });
        subSettings.add(channelStringElement);

        /* TWITCH OAUTH TOKEN */
        StringElement tokenStringElement = new StringElement( "Twitch Token",
                new ControlElement.IconData( Material.COMMAND ),
                twitchtoken, new Consumer<String>() {
            @Override
            public void accept(String token) {
                StreamChat.this.twitchtoken = token;

                StreamChat.this.getConfig().addProperty("twitchtoken", token);
                StreamChat.this.saveConfig();
                loadConfig();
            }
        });
        subSettings.add(tokenStringElement);

        /* SUPPORT NOTICE */
        subSettings.add(new HeaderElement("§7§oYou can get your twitch token from https://twitchapps.com/tmi/", 0.7));
    }
}
