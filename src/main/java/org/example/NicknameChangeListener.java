package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;

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
                    String oldName = member.getNickname() != null? member.getNickname() : member.getUser().getName();
                    // change nickname
                    guild.modifyNickname(member, newName).queue(response->
                            sendResponse(guild, channel, member.getUser(), oldName, newName)
                    );

                } catch (HierarchyException e) {
                    event.getChannel().sendMessage("Nickname change failed due to role hierarchy restrictions").queue();
                }
            }
        }
    }

    private void sendResponse(Guild guild, TextChannel channel, User user, String oldName, String newName) {
        if (!user.isBot()) {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setColor(Color.PINK)
                    .setTitle("Nickname Has Been Updated")
                    .setTimestamp(Instant.now())
                    .setAuthor(user.getName(), null, user.getAvatarUrl())
                    .addField("Former Nickname", oldName, false)
                    .addField("Updated Nickname", newName, false)
                    .setThumbnail(user.getAvatarUrl())
                    .setFooter(guild.getName(), guild.getIconUrl());


            channel.sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }
}
