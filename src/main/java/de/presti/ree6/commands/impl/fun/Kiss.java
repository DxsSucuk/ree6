package de.presti.ree6.commands.impl.fun;

import de.presti.ree6.commands.Category;
import de.presti.ree6.commands.Command;
import de.presti.ree6.utils.Neko4JsAPI;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import pw.aru.api.nekos4j.image.Image;
import pw.aru.api.nekos4j.image.ImageProvider;

public class Kiss extends Command {

    public Kiss() {
        super("kiss", "Kiss someone!", Category.FUN);
    }

    @Override
    public void onPerform(Member sender, Message messageSelf, String[] args, TextChannel m) {
        if (args.length == 1) {
            if(messageSelf.getMentionedMembers().isEmpty()) {
                sendMessage("No User mentioned!", 5, m);
                sendMessage("Use ree!kiss @user", 5, m);
            } else {

                User target = messageSelf.getMentionedMembers().get(0).getUser();

                sendMessage(sender.getAsMention() + " kissed " + target.getAsMention(), m);

                ImageProvider ip = Neko4JsAPI.imageAPI.getImageProvider();

                Image im = ip.getRandomImage("kiss").execute();

                sendMessage(im.getUrl(), m);
            }
        } else {
            sendMessage("Not enough Arguments!", 5, m);
            sendMessage("Use ree!kiss @user", 5, m);
        }
    }
}
