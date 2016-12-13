package com.godson.kekbot.commands.music;

import com.darichey.discord.api.Command;
import com.godson.kekbot.KekBot;
import com.godson.kekbot.Responses.Action;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.Optional;

public class Playlist {
    public static Command playlist = new Command("playlist")
            .withDescription("")
            .withUsage("{p}playlist")
            .onExecuted(context -> {
                Optional<VoiceChannel> voiceChannel = context.getGuild().getVoiceChannels().stream().filter(c -> c.getMembers().contains(context.getMember())).findFirst();
                if (!voiceChannel.isPresent()) {
                    context.getTextChannel().sendMessage("This command requies you to be in a voice channel!").queue();
                } else {
                    if (context.getGuild().getAudioManager().getConnectedChannel().equals(voiceChannel.get())) {
                        if (context.getArgs().length > 0) {
                            try {
                                int page = Integer.valueOf(context.getArgs()[0]);
                                KekBot.player.getPlaylist(context.getTextChannel(), page - 1);
                            } catch (NumberFormatException e) {
                                context.getTextChannel().sendMessage(KekBot.respond(context, Action.NOT_A_NUMBER, context.getArgs()[0])).queue();
                            }
                        } else {
                            KekBot.player.getPlaylist(context.getTextChannel(), 0);
                        }
                    } else context.getTextChannel().sendMessage("You have to be in \"" + context.getGuild().getAudioManager().getConnectedChannel().getName() + "\" in order to use music commands.").queue();
                }
            });
}