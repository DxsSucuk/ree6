package de.presti.ree6.commands;

import de.presti.ree6.commands.impl.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class CommandManager {

    static String prefix = "ree!";

    static ArrayList<Command> cmds = new ArrayList<>();

    public CommandManager() {

        addCommand(new Help());

        addCommand(new Clear());
        addCommand(new Setup());
        addCommand(new Mute());
        addCommand(new Unmute());
        addCommand(new Kick());
        addCommand(new Ban());
        addCommand(new Unban());

        addCommand(new Play());
        addCommand(new Pause());
        addCommand(new Resume());
        addCommand(new Stop());
        addCommand(new Disconnect());
        addCommand(new Skip());
        addCommand(new Loop());
        addCommand(new Volume());
        addCommand(new Clearqueue());
        addCommand(new Songlist());

    }

    public void addCommand(Command c) {
        if (!cmds.contains(c)) {
            cmds.add(c);
        }
    }

    public void perform(Member sender, String msg, Message messageSelf, TextChannel m) {

        if (!msg.startsWith(prefix))
            return;

        msg = msg.substring(prefix.length());
        String[] oldargs = msg.split(" ");
        for (Command cmd : getCommands()) {
            if (cmd.getCmd().equalsIgnoreCase(oldargs[0])) {
                String[] args = (String[]) Arrays.copyOfRange(oldargs, 1, oldargs.length);
                cmd.onPerform(sender, messageSelf, args, m);
                break;
            }
        }
    }

    public void removeCommand(Command c) {
        if (cmds.contains(c)) {
            cmds.remove(c);
        }
    }

    public ArrayList<Command> getCommands() {
        return cmds;
    }

    public static void sendMessage(String msg, MessageChannel m) {
        m.sendMessage(msg).queue();
    }

    public static void sendMessage(String msg, int deletesecond, MessageChannel m) {
        m.sendMessage(msg).delay(deletesecond, TimeUnit.SECONDS).flatMap(Message::delete).queue();
    }


    public static void sendMessage(EmbedBuilder msg, MessageChannel m) {
        m.sendMessage(msg.build()).queue();
    }

    public static void sendMessage(EmbedBuilder msg, int deletesecond, MessageChannel m) {
        m.sendMessage(msg.build()).delay(deletesecond, TimeUnit.SECONDS).flatMap(Message::delete).queue();
    }

}