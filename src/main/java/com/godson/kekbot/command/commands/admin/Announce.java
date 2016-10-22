package com.godson.kekbot.command.commands.admin;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandCategory;
import com.darichey.discord.api.CommandRegistry;
import com.godson.kekbot.EasyMessage;
import com.godson.kekbot.KekBot;
import com.godson.kekbot.XMLUtils;
import org.jdom2.JDOMException;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.Permissions;

import java.io.IOException;
import java.util.EnumSet;

public class Announce {
    public static Command announce = new Command("announce")
            .withCategory(CommandCategory.ADMIN)
            .withDescription("Allows you to config various \"announcement\" settings, including the welcome message, farewell message, and KekBot's broadcasts.")
            .withUsage("{p}announce <welcome|farewell|broadcasts>")
            .userRequiredPermissions(EnumSet.of(Permissions.ADMINISTRATOR))
            .onExecuted(context -> {
                IChannel channel = context.getMessage().getChannel();
                String prefix = CommandRegistry.getForClient(KekBot.client).getPrefixForGuild(context.getMessage().getGuild()) == null
                        ? CommandRegistry.getForClient(KekBot.client).getPrefix()
                        : CommandRegistry.getForClient(KekBot.client).getPrefixForGuild(context.getMessage().getGuild());
                String rawSplit[] = context.getMessage().getContent().split(" ", 4);
                String serverID = context.getMessage().getGuild().getID();
                if (rawSplit.length == 1) {
                    EasyMessage.send(channel, "```md\n[Command](announce)" +
                            "\n\n[Category](Administration)" +
                            "\n\n[Description](Allows you to config various \"announcement\" settings, including the welcome message, farewell message, and KekBot's broadcasts.)" +
                            "\n\n# Paramaters (<> Required, {} Optional)" +
                            "\n[Usage](" + prefix + "announce <welcome|farewell|broadcasts>))```");
                } else {
                    try {
                        switch (rawSplit[1]) {
                            case "welcome":
                                if (rawSplit.length == 2) {
                                    EasyMessage.send(channel, "```md\n[Subcommand](announce welcome)" +
                                            "\n\n[Description](Allows the user to set the welcome message, the channel the message will be sent to, as well as review their server's settings.)" +
                                            "\n\n# Paramaters (<> Required, {} Optional)" +
                                            "\n[Usage](" + prefix + "announce welcome <message|channel|review>)```");
                            } else {
                                    switch (rawSplit[2]) {
                                        case "message":
                                            if (rawSplit.length >= 4) {
                                                if (rawSplit[3].equals("reset")) {
                                                    XMLUtils.deleteWelcomeMessage(serverID);
                                                    EasyMessage.send(channel, "Done, I will no longer remember what to tell people when they join this server.");
                                                } else {
                                                    XMLUtils.setWelcomeMessage(serverID, channel, rawSplit[3]);
                                                }
                                            } else {
                                                EasyMessage.send(channel, "What's the message? :neutral_face:");
                                            }
                                            break;
                                        case "channel":
                                            if (rawSplit.length >= 4) {
                                                if (rawSplit[3].equals("reset")) {
                                                    XMLUtils.deleteWelcomeChannel(serverID);
                                                    EasyMessage.send(channel, "Done, I will no longer remember what channel to welcome people in.");
                                                } else {
                                                    if (context.getMessage().getChannelMentions().size() == 0) {
                                                        EasyMessage.send(channel, "The channel you want to assign welcomes to *must* be in the form of a mention1");
                                                    } else {
                                                        XMLUtils.setWelcomeChannel(serverID, channel, context.getMessage().getChannelMentions().get(0));
                                                    }
                                                }
                                            } else {
                                                EasyMessage.send(channel, "Where am I supposed to welcome new people? :neutral_face:");
                                            }
                                            break;
                                        case "review":
                                            XMLUtils.reviewWelcomeSettings(serverID, channel);
                                            break;
                                    }

                                }
                                break;
                            case "farewell":
                                if (rawSplit.length == 2) {
                                    EasyMessage.send(channel, "```md\n[Subcommand](announce farewell)" +
                                            "\n\n[Description](Allows the user to set the farewell message, the channel the message will be sent to, as well as review their server's settings.)" +
                                            "\n\n# Paramaters (<> Required, {} Optional)" +
                                            "\n[Usage](" + prefix + "announce farewell <message|channel|review>)```");
                                } else {
                                    switch (rawSplit[2]) {
                                        case "message":
                                            if (rawSplit.length == 4) {
                                                if (rawSplit[3].equals("reset")) {
                                                    XMLUtils.deleteGoodbyeMessage(serverID);
                                                    EasyMessage.send(channel, "Done, I will go back to using the default message.");
                                                } else {
                                                    XMLUtils.setGoodbyeMessage(serverID, channel, rawSplit[3]);
                                                }
                                            } else {
                                                EasyMessage.send(channel, "What do you want me to tell everyone when people leave? :neutral_face:");
                                            }
                                            break;
                                        case "channel":
                                            if (rawSplit.length == 4) {
                                                if (rawSplit[3].equals("reset")) {
                                                    XMLUtils.deleteGoodbyeChannel(serverID);
                                                    EasyMessage.send(channel, "Done, I will no longer remember where to announce people leaving this server.");
                                                } else {
                                                    if (context.getMessage().getChannelMentions().size() == 0) {
                                                        EasyMessage.send(channel, "The channel you want to assign welcomes to *must* be in the form of a mention1");
                                                    } else {
                                                        XMLUtils.setGoodbyeChannel(serverID, channel, context.getMessage().getChannelMentions().get(0));
                                                        EasyMessage.send(channel, "Alright, I will let everyone know when someone leaves in " + context.getMessage().getChannelMentions().get(0).mention() + ". :thumbsup:");
                                                    }
                                                }
                                            } else {
                                                EasyMessage.send(channel, "Where do you want me to tell everyone about people leaving? :neutral_face:");
                                            }
                                            break;
                                        case "review":
                                            XMLUtils.reviewFarewellSettings(serverID, channel);
                                            break;
                                    }
                                }
                                break;
                            case "broadcasts":
                                if (rawSplit.length == 2) {
                                    EasyMessage.send(channel, "```md\n[Subcommand](announce broadcasts)" +
                                            "\n\n[Description](Allows the user to enable or disabled KekBot's broadcasts, set the channel KekBot's broadcats will be sent to, as well as review their server's settings.)" +
                                            "\n\n# Paramaters (<> Required, {} Optional)" +
                                            "\n[Usage](" + prefix + "announce broadcasts <channel|enable|disable|review>)```");
                                } else {
                                    switch (rawSplit[2]) {
                                        case "channel":
                                            if (rawSplit.length == 4) {
                                                if (rawSplit[3].equals("reset")) {
                                                    XMLUtils.deleteBroadcastsChannel(serverID);
                                                    EasyMessage.send(channel, "Done, I will revert to using the first channel I find.");
                                                } else {
                                                    if (context.getMessage().getChannelMentions().size() == 0) {
                                                        EasyMessage.send(channel, "The channel you want to assign welcomes to *must* be in the form of a mention1");
                                                    } else {
                                                        XMLUtils.setBroadcastsChannel(serverID, context.getMessage().getChannelMentions().get(0).getID());
                                                        EasyMessage.send(channel, "Alright, all future broadcasts will be posted in " + context.getMessage().getChannelMentions().get(0).mention() + ". :thumbsup:");
                                                    }
                                                }
                                            } else {
                                                EasyMessage.send(channel, "Where do you want Broadcasts to be sent? :neutral_face:");
                                            }
                                            break;
                                        case "enable":
                                            XMLUtils.enableBroadcasts(serverID, channel);
                                            break;
                                        case "disable":
                                            XMLUtils.disableBroadcasts(serverID, channel);
                                            break;
                                        case "review":
                                            XMLUtils.reviewBroadcastSettings(serverID, channel);
                                            break;
                                    }
                                }
                        }
                    } catch (JDOMException | IOException e) {
                        e.printStackTrace();
                    }
                }
            })
            .onFailure((context, reason) -> EasyMessage.send(context.getMessage().getChannel(), context.getMessage().getAuthor().mention() + ", you don't have the `Administrator` permission!")
            );
}
