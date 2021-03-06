package de.presti.ree6.commands.impl.mod;

import de.presti.ree6.commands.Category;
import de.presti.ree6.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.function.Consumer;

public class Unban extends Command {

    public Unban() {
        super("unban", "Unban a User from the Server!", Category.MOD);
    }

    @Override
    public void onPerform(Member sender, Message messageSelf, String[] args, TextChannel m) {
        if (sender.hasPermission(Permission.ADMINISTRATOR)) {
            if (args.length == 1) {
                String givenid = args[1];
                m.getGuild().unban(givenid).queue();
                sendMessage("User <@" + givenid + "> has been unbanned!", 5, m);
            } else {
                sendMessage("Not enough Arguments!", 5, m);
                sendMessage("Use ree!unban @user", 5, m);
            }
        } else {
            sendMessage("You dont have the Permission for this Command!", 5, m);
        }

        messageSelf.delete().queue();
    }
}