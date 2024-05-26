package org.example;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NicknameChangeListener extends ListenerAdapter {
    private final SlashCommandListener commandListener;

    NicknameChangeListener(SlashCommandListener commandListener) {
        this.commandListener = commandListener;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = commandListener.getTextChannelById(guild);

        if (channel != null && event.getChannel().equals(channel)) {
            String newName = event.getMessage().getContentRaw();
            Member member = event.getMember();

            if (member != null) {
                try {
                    guild.modifyNickname(member, newName).queue();
                } catch (HierarchyException e) {
                    event.getChannel().sendMessage("Unable to change the nickname of this member due to role hierarchy!").queue();
                }
            }
        }
    }
}
