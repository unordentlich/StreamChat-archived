package de.stornierung.streamchat.listener;

import de.stornierung.streamchat.StreamChat;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.main.LabyMod;

public class IngameChat implements MessageSendEvent {

    @Override
    public boolean onSend(String s) {
        if (StreamChat.chatenabled) {
            if (StreamChat.bot == null) {
                LabyMod.getInstance().displayMessageInChat("§eStream§7Chat§6+ §8» §cMessage could not be sent in TwitchChat! :(");
            } else {
                if (!StreamChat.bot.isConnected()) {
                    LabyMod.getInstance().displayMessageInChat("§eStream§7Chat§6+ §8» §cMessage could not be sent in TwitchChat! :(");
                } else {
                    if (s.startsWith(StreamChat.ingamePrefix)) {
                        String message = s.replaceFirst(StreamChat.ingamePrefix, "");
                        StreamChat.bot.sendMessage("#" + StreamChat.twitchChannel, message);
                        LabyMod.getInstance().displayMessageInChat(StreamChat.prefix.replace("%user%", LabyMod.getInstance().getPlayerName())
                                .replace("%message%", message).replace("@" + StreamChat.twitchChannel,
                                        "§l@" + StreamChat.twitchChannel));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
