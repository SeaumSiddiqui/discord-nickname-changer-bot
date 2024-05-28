package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
    public static void main(String[] args) {
        final String token = System.getenv("BOT_TOKEN");
        if (token == null || token.isEmpty()) {
            System.err.println("Bot token not found");
            return;
        }
        SlashCommandListener slashCommandListener = new SlashCommandListener();
        NicknameChangeListener nicknameChangeListener = new NicknameChangeListener(slashCommandListener);

        try {
            JDA jda = JDABuilder
                    .createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(
                            slashCommandListener,
                            nicknameChangeListener
                    )
                    .build();
            jda.awaitReady();

            // register slash command
            jda.upsertCommand(
                    Commands.slash("aqiqah", "Assign the bot to this channel")
            ).queue();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}