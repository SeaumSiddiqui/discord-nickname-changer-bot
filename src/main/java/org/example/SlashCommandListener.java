package org.example;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class SlashCommandListener extends ListenerAdapter {
    private final Map<String, String> guildChannelMap = new HashMap<>();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("aqiqah")){
            Guild guild = event.getGuild();
            String channel = event.getChannelId();

            if (guild != null) {
                guildChannelMap.put(guild.getId(), channel);
                event.reply("Channel Assigned").setEphemeral(true).queue();
            }
        }
    }

    public TextChannel getTextChannelById (Guild guild) {
        String channelId = guildChannelMap.get(guild.getId());
        return (channelId != null)? guild.getTextChannelById(channelId): null;
    }
}
