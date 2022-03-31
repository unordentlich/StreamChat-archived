package de.stornierung.streamchat.api;

import de.stornierung.streamchat.StreamChat;
import net.labymod.main.LabyMod;
import net.minecraftforge.common.ForgeVersion;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import java.io.IOException;

import static de.stornierung.streamchat.StreamChat.*;

public class TwitchBot extends PircBot {

    public static void startBot() {
        if (enabled) {
            if (twitchenabled) {
                bot = new TwitchBot();
                bot.setVerbose(true);
                try {
                    bot.connect("irc.twitch.tv", 6667, StreamChat.twitchtoken);
                    token_working = true;
                } catch (IOException e) {
                    token_working = false;
                } catch (IrcException e) {
                    token_working = false;
                }
                bot.joinChannel("#" + twitchChannel.toLowerCase());
            }
        }
    }

    public static void stopBot() {
        bot.disconnect();
    }

    public TwitchBot() {
        this.setName("StreamChatPlus");
        this.isConnected();
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.equalsIgnoreCase("!server")) {
            if (StreamChat.server_cmd) {
                if (LabyMod.getInstance().getCurrentServerData() != null) {
                    bot.sendMessage(channel, LabyMod.getInstance().getPlayerName() +
                            " plays on " + LabyMod.getInstance().getCurrentServerData().getIp());
                } else {
                    bot.sendMessage(channel, LabyMod.getInstance().getPlayerName() +
                            " isn't playing on any server at this moment...");
                }
            }
        } else if (message.equalsIgnoreCase("!version")) {
            if (StreamChat.version_cmd) {
                bot.sendMessage(channel, LabyMod.getInstance().getPlayerName()
                        + " plays with Minecraft " + ForgeVersion.getTarget());
            }
        } else if (message.equalsIgnoreCase("!name")) {
            if (StreamChat.name_cmd) {
                bot.sendMessage(channel, "The streamer's ingame name is "
                        + LabyMod.getInstance().getPlayerName());
            }
        } else {
            if (StreamChat.twitchenabled) {
                LabyMod.getInstance().displayMessageInChat(StreamChat.prefix.replace("%user%", sender)
                        .replace("%message%", message).replace("@" + StreamChat.twitchChannel,
                                "§l@" + StreamChat.twitchChannel));
            }
        }
    }
}
