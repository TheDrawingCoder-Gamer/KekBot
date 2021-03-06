package com.godson.kekbot.commands.admin;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandCategory;
import com.darichey.discord.api.FailureReason;
import com.godson.kekbot.GSONUtils;
import com.godson.kekbot.KekBot;
import com.godson.kekbot.Responses.Action;
import com.godson.kekbot.Settings.Settings;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

public class AutoRole {
    public static Command autoRole = new Command("autorole")
            .withCategory(CommandCategory.ADMIN)
            .withDescription("Allows you to set a role in which KekBot will assign to all newcomers. You can also RESET this setting, disabling it.")
            .withUsage("{p}autorole <role | reset>")
            .userRequiredPermissions(Permission.MANAGE_ROLES)
            .onExecuted(context -> {
                String rawSplit[] = context.getMessage().getRawContent().split(" ", 2);
                TextChannel channel = context.getTextChannel();
                Guild guild = context.getGuild();
                Settings settings = GSONUtils.getSettings(guild);
                    if (rawSplit.length == 1) {
                        channel.sendMessage("Which role am I gonna automatically give newcomers? :neutral_face:").queue();
                    } else {
                        if (guild.getRolesByName(rawSplit[1], true).size() == 0) {
                            channel.sendMessage("Unable to find any roles by the name of \"" + rawSplit[1] + "\"!").queue();
                        } else {
                            settings.setAutoRoleID(guild.getRolesByName(rawSplit[1], true).get(0).getId());
                            settings.save(context.getGuild());
                            channel.sendMessage("Got it! I will now give newcomers the role \"" + guild.getRolesByName(rawSplit[1], true).get(0).getName() + "\"!").queue();
                        }
                    }
            })
            .onFailure((context, reason) -> {
                if (reason.equals(FailureReason.AUTHOR_MISSING_PERMISSIONS))
                    context.getTextChannel().sendMessage(KekBot.respond(context, Action.NOPERM_USER, "`Manage Roles`")).queue();
            });
}
